package at.joma.apidesign.component.l2.provider.impl;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.types.ConfiguredOption;
import at.joma.apidesign.component.l2.client.api.types.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.provider.api.AsXML;
import at.joma.apidesign.component.l2.provider.api.builder.AsXMLByBuilder;

@Sorting
@Omitting
public class ComponentProducer {

	@Produces
	@AsXMLByBuilder
	public IL2Component doProduceForBuilder(InjectionPoint ip) throws NoSuchMethodException {
		AsXMLByBuilder configuration = (AsXMLByBuilder) ip.getQualifiers().iterator().next();
		return createWithOptions(configuration.sorting().order(), configuration.sorting().direction(), configuration.ommiting().globalFields());
	}

	@Produces
	@AsXML
	public IL2Component doProduceForCDI(InjectionPoint ip) throws NoSuchMethodException {

		this.getClass().getAnnotation(Sorting.class).direction();

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

		return createWithOptions(orderOption, directionOption, globalFieldsOption);
	}

	private IL2Component createWithOptions(SortingOrder orderOption, SortingDirection directionOption, String[] globalFieldsOption) {
		Configured iL2Component = new Configured(orderOption, directionOption, globalFieldsOption);

		return iL2Component;
	}

	public static class Configured implements IL2Component {

		public static final String GLOBALFIELDS_OPTIONNAME = "globalFields";

		public ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
		
		public Configured(SortingOrder orderOption, SortingDirection directionOption, String[] globalFieldsOption){
			this.configuredOptions.with(orderOption);
			this.configuredOptions.with(directionOption);
			this.configuredOptions.with(GLOBALFIELDS_OPTIONNAME, globalFieldsOption);
		}

		@Override
		public String serialize(Object serializable) {
			return null;
		}

		@Override
		public String printConfiguration() {
			return configuredOptions.printConfiguration();
		}

		@Override
		public ConfiguredOption[] getConfiguration() {
			return configuredOptions.getConfiguration();
		}

		@Override
		public int size() {
			return configuredOptions.size();
		}
	}

}
