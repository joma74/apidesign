package at.joma.apidesign.component.l2.provider.impl;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;

import at.joma.apidesign.component.l1.client.api.config.IConfiguration;
import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.client.api.types.config.Option;
import at.joma.apidesign.component.l2.provider.api.AsXML;
import at.joma.apidesign.component.l2.provider.api.builder.AsXMLByBuilder;

@Sorting
@Omitting
public class ComponentProducer {

    @Inject
    @Named(ComponentCacheHolder.CDI_NAME)
    private ComponentCacheHolder il2componentCacheHolder;

    protected static final Map<String, String> FORMATINFOS = new HashMap<>();

    static {
        FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_FORMAT, "XML");
        FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_PRODUCER, ComponentProducer.class.getName());
    }

    public static class Component implements IL2Component {

        public static final String GLOBALFIELDS_OPTIONNAME = "globalFields";

        public static final String FORMATINFO_KEY_FORMATTER = "XStream";

        protected static final Map<String, String> FORMATINFOS = new HashMap<>();

        static {
            FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_FORMATTER, FORMATINFO_KEY_FORMATTER);
            FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_COMPONENT, Component.class.getName());
        }

        private WeakReference<ConfiguredOptionsHolder> configuredOptions = new WeakReference<ConfiguredOptionsHolder>(new ConfiguredOptionsHolder());

        public Component(ConfiguredOptionsHolder configuredOptions) {
            ConfiguredOptionsHolder clone = (ConfiguredOptionsHolder) configuredOptions.deepClone();
            encloseFormatInfo(clone);
            this.configuredOptions = new WeakReference<ConfiguredOptionsHolder>(clone);
        }

        public Component(Map<String, String> formatInfos, SortingOrder orderOption, SortingDirection directionOption, String[] globalFieldsOption) {
            this.configuredOptions.get()//
                    .encloseFormatInfos(formatInfos)//
                    .with(orderOption)//
                    .with(directionOption)//
                    .with(GLOBALFIELDS_OPTIONNAME, globalFieldsOption)//
            ;
            encloseFormatInfo(this.configuredOptions.get());
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
            return null;
        }

        @Override
        public IConfiguration deepClone() {
            return this.configuredOptions.get().deepClone();
        }
    }

    private IL2Component createWithOptions(Map<String, String> formatInfos, SortingOrder orderOption, SortingDirection directionOption, String[] globalFieldsOption) {

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder()//
                .encloseFormatInfos(formatInfos)//
                .with(orderOption)//
                .with(directionOption)//
                .with(Component.GLOBALFIELDS_OPTIONNAME, globalFieldsOption)//
        ;

        Component iL2Component = il2componentCacheHolder.getIfPresent(configuredOptions);
        if (iL2Component == null) {
            iL2Component = new Component(configuredOptions);
            il2componentCacheHolder.put(configuredOptions, iL2Component);
        }
        return iL2Component;
    }

    @Produces
    @AsXMLByBuilder
    public IL2Component doProduceForBuilder(InjectionPoint ip) throws NoSuchMethodException {

        AsXMLByBuilder configuration = (AsXMLByBuilder) ip.getQualifiers().iterator().next();
        return createWithOptions(FORMATINFOS, configuration.sorting().order(), configuration.sorting().direction(), configuration.ommiting().globalFields());
    }

    @Produces
    @AsXML
    public IL2Component doProduceForCDI(InjectionPoint ip) throws NoSuchMethodException {

        SortingOrder orderOption = this.getClass().getAnnotation(Sorting.class).order();
        SortingDirection directionOption = this.getClass().getAnnotation(Sorting.class).direction();
        String[] globalFieldsOption = this.getClass().getAnnotation(Omitting.class).globalFields();

        Annotated annotated = ip.getAnnotated();

        if (annotated != null) {

            Sorting sortingAnnotation = annotated.getAnnotation(Sorting.class);

            Omitting omittingAnnotation = annotated.getAnnotation(Omitting.class);

            if (sortingAnnotation != null) {
                orderOption = sortingAnnotation.order();
                directionOption = sortingAnnotation.direction();
            }

            if (omittingAnnotation != null) {
                globalFieldsOption = omittingAnnotation.globalFields();
            }
        }

        return createWithOptions(FORMATINFOS, orderOption, directionOption, globalFieldsOption);
    }
    
    public static Map<String, String> getFormatInfos() {
        return Collections.unmodifiableMap(FORMATINFOS);
    }

}
