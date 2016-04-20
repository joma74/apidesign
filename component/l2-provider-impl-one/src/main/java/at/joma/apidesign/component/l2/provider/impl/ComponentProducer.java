package at.joma.apidesign.component.l2.provider.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.config.IConfiguration;
import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.provider.api.AsXML;
import at.joma.apidesign.component.l2.provider.api.builder.AsXMLByBuilder;

@Sorting
@Omitting
public class ComponentProducer {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentProducer.class);

    private static Object SEMAPHORE = new Object();

    @Inject
    BeanManager beanManager;

    @Inject
    @Named(ComponentCacheHolder.CDI_NAME)
    ComponentCacheHolder il2componentCacheHolderDONOTUSE;

    protected static final Map<String, String> FORMATINFOS = new HashMap<>();

    static {
        FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_FORMAT, "XML");
        FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_PRODUCER, ComponentProducer.class.getName());
    }

    private IL2Component createWithOptions(ComponentCacheHolder il2componentCacheHolder, Map<String, String> formatInfos, SortingOrder orderOption,
            SortingDirection directionOption, String[] globalFieldsOption) {

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder()//
                .encloseFormatInfos(formatInfos)//
                .encloseFormatInfos(Component.getFormatInfos())// else the
                                                               // reflective
                                                               // hashCode
                                                               // check for
                                                               // sameness
                                                               // fails
                .with(orderOption)//
                .with(directionOption)//
                .with(Component.GLOBALFIELDS_OPTIONNAME, globalFieldsOption)//
        ;

        Component iL2Component = il2componentCacheHolder.getIfPresent(configuredOptions);
        if (iL2Component == null) {
            synchronized (new Integer(configuredOptions.hashCode())) {
                iL2Component = il2componentCacheHolder.getIfPresent(configuredOptions);
                if (iL2Component == null) {
                    if (LOG.isDebugEnabled()) {
                        for (Entry<ConfiguredOptionsHolder, Component> entry : il2componentCacheHolder.getCache().asMap().entrySet()) {
                            ConfiguredOptionsHolder coh = entry.getKey();
                            LOG.debug(System.lineSeparator() + "ComponentCacheHolder Report for Key " + ConfiguredOptionsHolder.class.getSimpleName()
                                    + " having a hashcode of " + coh.hashCode() + " and represents a configuration of " + coh.printConfiguration());
                        }
                    }
                    iL2Component = new Component(configuredOptions);
                    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
                    Validator validator = validatorFactory.getValidator();
                    Set<ConstraintViolation<Component>> constraintViolations = validator.validate(iL2Component);
                    if (!constraintViolations.isEmpty()) {
                        throw new ConstraintViolationException(Component.ERROR_MESSAGE_OPTIONSNOTVALID, constraintViolations);
                    }
                    il2componentCacheHolder.put(configuredOptions, iL2Component);
                }
            }
        }
        return iL2Component;
    }

    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    @Produces
    @AsXMLByBuilder
    public IL2Component doProduceForBuilder(InjectionPoint ip) throws NoSuchMethodException {

        AsXMLByBuilder configuration = (AsXMLByBuilder) ip.getQualifiers().iterator().next();

        Bean<ComponentCacheHolder> targetedBean = (Bean<ComponentCacheHolder>) beanManager.resolve(beanManager.getBeans(ComponentCacheHolder.class));

        Context beanContextOfConfig = beanManager.getContext(configuration.inScope());

        CreationalContext creationalContextOfIP = beanManager.createCreationalContext(null);

        ComponentCacheHolder il2componentCacheHolder = beanContextOfConfig.get(targetedBean, creationalContextOfIP);

        return createWithOptions(il2componentCacheHolder, FORMATINFOS, configuration.sorting().order(), configuration.sorting().direction(), configuration.ommiting()
                .globalFields());
    }

    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    @Produces
    @AsXML
    public IL2Component doProduceForCDI(InjectionPoint ip) throws NoSuchMethodException {

        SortingOrder orderOption = this.getClass().getAnnotation(Sorting.class).order();
        SortingDirection directionOption = this.getClass().getAnnotation(Sorting.class).direction();
        String[] globalFieldsOption = this.getClass().getAnnotation(Omitting.class).globalFields();

        Bean<ComponentCacheHolder> targetedBean = (Bean<ComponentCacheHolder>) beanManager.resolve(beanManager.getBeans(ComponentCacheHolder.class));
        
        ComponentCacheHolder il2componentCacheHolder;
        
        if(ip.getBean() != null){
            
            Context beanContextOfIP = beanManager.getContext(ip.getBean().getScope());

            CreationalContext creationalContextOfIP = beanManager.createCreationalContext(ip.getBean());

            il2componentCacheHolder = beanContextOfIP.get(targetedBean, creationalContextOfIP);
        } else {
            
            AsXML configuration = (AsXML) ip.getQualifiers().iterator().next();
            
            Context beanContextOfConfig = beanManager.getContext(configuration.inScope());

            CreationalContext creationalContextOfIP = beanManager.createCreationalContext(null);

            il2componentCacheHolder = beanContextOfConfig.get(targetedBean, creationalContextOfIP);
        }

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

        return createWithOptions(il2componentCacheHolder, FORMATINFOS, orderOption, directionOption, globalFieldsOption);
    }

    public static Map<String, String> getFormatInfos() {
        return Collections.unmodifiableMap(FORMATINFOS);
    }

}
