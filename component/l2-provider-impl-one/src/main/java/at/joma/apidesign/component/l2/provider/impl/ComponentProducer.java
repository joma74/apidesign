package at.joma.apidesign.component.l2.provider.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

import at.joma.apidesign.component.l1.client.api.types.ConfiguredOption;
import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.f.Omitting;
import at.joma.apidesign.component.l2.client.api.f.Sorting;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;
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
		Configured iL2Component = new Configured();
		iL2Component.orderOption = orderOption;
		iL2Component.directionOption = directionOption;
		iL2Component.globalFieldsOption = globalFieldsOption;

		return iL2Component;
	}

	public static class Configured implements IL2Component {

		private static final String TAB = "\t";

		public SortingOrder orderOption;

		public SortingDirection directionOption;

		public static final String GLOBALFIELDS_OPTIONNAME = "globalFields";
		
		public String[] globalFieldsOption;

		@Override
		public String serialize(Object serializable) {
			return null;
		}

		@Override
		public String printConfigurationOptions() {
			StringBuilder configuration = new StringBuilder(System.lineSeparator() + "Configuration" + System.lineSeparator());
			ConfiguredOption[] options = getConfiguration();
			for(ConfiguredOption option : options){
				configuration.append(TAB + option.name + ":" + option.value + System.lineSeparator());				
			}
			return configuration.toString();
		}

		@Override
		public ConfiguredOption[] getConfiguration() {
			List<ConfiguredOption> configuredOptions = new ArrayList<ConfiguredOption>();
			configuredOptions.add(new ConfiguredOption(orderOption));
			configuredOptions.add(new ConfiguredOption(directionOption));
			configuredOptions.add(new ConfiguredOption(GLOBALFIELDS_OPTIONNAME, globalFieldsOption));
			return configuredOptions.toArray(new ConfiguredOption[]{});
		}
	}

}
