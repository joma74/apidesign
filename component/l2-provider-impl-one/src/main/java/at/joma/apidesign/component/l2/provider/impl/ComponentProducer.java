package at.joma.apidesign.component.l2.provider.impl;

import java.lang.ref.WeakReference;

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

	public static class Configured implements IL2Component {

		public static final String GLOBALFIELDS_OPTIONNAME = "globalFields";

		private WeakReference<ConfiguredOptionsHolder> configuredOptions = new WeakReference<ConfiguredOptionsHolder>(new ConfiguredOptionsHolder());

		public Configured(ConfiguredOptionsHolder configuredOptions) {
			this.configuredOptions = new WeakReference<ConfiguredOptionsHolder>(new ConfiguredOptionsHolder());
		}

		public Configured(SortingOrder orderOption, SortingDirection directionOption, String[] globalFieldsOption) {
			this.configuredOptions.get().with(orderOption);
			this.configuredOptions.get().with(directionOption);
			this.configuredOptions.get().with(GLOBALFIELDS_OPTIONNAME, globalFieldsOption);
		}

		@Override
		public Option[] getConfiguration() {
			return this.configuredOptions.get().getConfiguration();
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

	private IL2Component createWithOptions(SortingOrder orderOption, SortingDirection directionOption, String[] globalFieldsOption) {

		ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
		configuredOptions.with(orderOption);
		configuredOptions.with(directionOption);
		configuredOptions.with(Configured.GLOBALFIELDS_OPTIONNAME, globalFieldsOption);

		Configured iL2Component = il2componentCacheHolder.getIfPresent(configuredOptions);
		if (iL2Component == null) {
			iL2Component = new Configured(orderOption, directionOption, globalFieldsOption);
			il2componentCacheHolder.put(configuredOptions, iL2Component);
		}
		return iL2Component;
	}

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

}
