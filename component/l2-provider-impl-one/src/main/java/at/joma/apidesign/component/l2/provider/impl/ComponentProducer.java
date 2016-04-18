package at.joma.apidesign.component.l2.provider.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l1.client.api.config.IConfiguration;
import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.provider.api.AsXML;
import at.joma.apidesign.component.l2.provider.api.builder.AsXMLByBuilder;

@ApplicationScoped
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

	private IL2Component createWithOptions(Map<String, String> formatInfos, SortingOrder orderOption, SortingDirection directionOption, String[] globalFieldsOption) {

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
			iL2Component = new Component(configuredOptions);
			il2componentCacheHolder.put(configuredOptions, iL2Component);
			ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = validatorFactory.getValidator();
			Set<ConstraintViolation<Component>> constraintViolations = validator.validate(iL2Component);
			if (!constraintViolations.isEmpty()) {
				throw new ConstraintViolationException(Component.ERROR_MESSAGE_OPTIONSNOTVALID, constraintViolations);
			}
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
