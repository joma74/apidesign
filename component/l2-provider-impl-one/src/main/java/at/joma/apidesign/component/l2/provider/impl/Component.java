package at.joma.apidesign.component.l2.provider.impl;

import static com.googlecode.cqengine.query.QueryFactory.and;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.config.IConfiguration;
import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.client.api.types.config.Option;
import at.joma.apidesign.component.l2.provider.api.SelfValidating;
import at.joma.apidesign.component.l2.provider.impl.xstream.AlphabetKeySorter;
import at.joma.apidesign.component.l2.provider.impl.xstream.ByteXmlConverter;
import at.joma.apidesign.component.l2.provider.impl.xstream.OmittingMapperWrapper;

import com.google.common.base.Preconditions;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.mapper.MapperWrapper;

@SelfValidating
public class Component implements IL2Component {

    /**
     * Logger to use.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Component.class);

    public static final String FORMATINFO_KEY_FORMATTER = XStream.class.getName();

    public static final String ERROR_MESSAGE_OPTIONSNOTVALID = "Options are not valid";

    protected static final Map<String, String> FORMATINFOS = new HashMap<>();

    protected static final IndexedCollection<ValidOptionsTuple> VALIDOPTIONSTUPLES = new ConcurrentIndexedCollection<>();

    /**
     * WELD method for supporting unproxying.
     */
    private static final String UNPROXY_METHOD_NAME_WELD = "getTargetInstance";

    /**
     * The to-be-used xStream instance.
     */
    private XStream configuredXStreamInstance;

    static {
        FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_FORMATTER, FORMATINFO_KEY_FORMATTER);
        FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_COMPONENT, Component.class.getName());

        VALIDOPTIONSTUPLES.add(new ValidOptionsTuple(SortingOrder.ALPHABETICALLY, SortingDirection.ASC));
        VALIDOPTIONSTUPLES.add(new ValidOptionsTuple(SortingOrder.ALPHABETICALLY, SortingDirection.DESC));
        VALIDOPTIONSTUPLES.add(new ValidOptionsTuple(SortingOrder.GIVEN, SortingDirection.NONE));
    }

    private WeakReference<ConfiguredOptionsHolder> configuredOptions = new WeakReference<>(new ConfiguredOptionsHolder());

    public Component(ConfiguredOptionsHolder configuredOptions) {
        encloseFormatInfo(configuredOptions);
        this.configuredOptions = new WeakReference<>(configuredOptions);
    }

    private void encloseFormatInfo(ConfiguredOptionsHolder configuredOptions) {
        configuredOptions//
                .encloseFormatInfos(FORMATINFOS)//
        ;
    }

    public static Map<String, String> getFormatInfos() {
        return Collections.unmodifiableMap(FORMATINFOS);
    }

    @Override
    public Option[] getOptions() {
        return this.configuredOptions.get().getOptions();
    }

    @Override
    public Map<String, String> getFormatInfo() {
        return this.configuredOptions.get().getFormatInfo();
    }

    @Override
    public int optionsCount() {
        return this.configuredOptions.get().optionsCount();
    }

    @Override
    public String printConfiguration() {
        return this.configuredOptions.get().printConfiguration();
    }

    @Override
    public String serialize(Object serializable) {
        Preconditions.checkNotNull(serializable);
        return this.configuredXStreamInstance.toXML(unproxyIfWeld(serializable));
    }

    @Override
    public IConfiguration deepClone() {
        return this.configuredOptions.get().deepClone();
    }

    @Override
    public boolean isValid() {
        SortingOrder sortingOrder = configuredOptions.get().getValueFor(SortingOrder.class);
        SortingDirection sortingDirection = configuredOptions.get().getValueFor(SortingDirection.class);

        Query<ValidOptionsTuple> isValidQuery =
                and(QueryFactory.in(ValidOptionsTuple.VOT_SORTINGORDER, sortingOrder), QueryFactory.in(ValidOptionsTuple.VOT_SORTINGDIRECTION, sortingDirection));

        return VALIDOPTIONSTUPLES.retrieve(isValidQuery).size() >= 1;
    }

    public void initializeSerializingInstance() {

        ConfiguredOptionsHolder options = configuredOptions.get();

        final String[] omitByFieldNames = (String[]) options.getValueFor(Omitting.BYFIELDNAMES_OPTIONNAME);
        @SuppressWarnings("unchecked")
        final Class<? extends Annotation>[] omitByFieldAnnotations = (Class<? extends Annotation>[]) options.getValueFor(Omitting.BYFIELDANNOTATIONS_OPTIONNAME);
        final Class<?>[] omitByFieldClasses = (Class<?>[]) options.getValueFor(Omitting.BYFIELDCLASSES_OPTIONNAME);

        if (SortingOrder.ALPHABETICALLY == options.getValueFor(SortingOrder.class)) {
            this.configuredXStreamInstance =
                    new XStream(new PureJavaReflectionProvider(new FieldDictionary(new AlphabetKeySorter(options.getValueFor(SortingDirection.class))))) {

                        @Override
                        protected MapperWrapper wrapMapper(MapperWrapper next) {
                            return new OmittingMapperWrapper(next, omitByFieldNames, omitByFieldAnnotations, omitByFieldClasses);
                        }

                    };
        } else {
            this.configuredXStreamInstance = new XStream() {

                @Override
                protected MapperWrapper wrapMapper(MapperWrapper next) {
                    return new OmittingMapperWrapper(next, omitByFieldNames, omitByFieldAnnotations, omitByFieldClasses);
                }

            };
        }

        configuredXStreamInstance.registerConverter(new ByteXmlConverter());

    }

    /**
     * Unproxying support only the WELD implementation of CDI.
     * <p>
     * IMPLEMENTATION REMARK
     * <p>
     * MUST never fail
     * 
     * @param object
     *            to unproxy
     * @return a potentially unproxied object
     */
    @SuppressWarnings("unchecked")
    public static <T> T unproxyIfWeld(final Object object) {
        Method theMethod = null;
        try {
            theMethod = object.getClass().getDeclaredMethod(UNPROXY_METHOD_NAME_WELD);
        } catch (NoSuchMethodException e) {
            LOG.trace("ignored exception", e);
        }

        try {
            return theMethod == null ? (T) object : (T) theMethod.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error while trying to unproxy " + object, e);
        }
    }

}
