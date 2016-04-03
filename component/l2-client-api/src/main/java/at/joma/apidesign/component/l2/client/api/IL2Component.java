package at.joma.apidesign.component.l2.client.api;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.builder.IProviderRequiredOptions;
import at.joma.apidesign.component.l2.client.api.builder.IWithConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.client.api.builder.IWithOmitting;
import at.joma.apidesign.component.l2.client.api.builder.IWithSorting;
import at.joma.apidesign.component.l2.client.api.builder.options.OmittingOptions;
import at.joma.apidesign.component.l2.client.api.builder.options.SortingOptions;
import at.joma.apidesign.component.l2.client.api.types.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;

public interface IL2Component extends IL1Component {

	@Sorting
	@Omitting
	public class Builder<T extends IProviderRequiredOptions & Annotation> implements IWithSorting<T>, IWithOmitting<T>, IWithConfiguredOptionsHolder<T> {

		private ConfiguredOptionsHolder configuredOptionsHolder = new ConfiguredOptionsHolder();

		private final Class<T> providerType;

		public Builder(Class<T> providerType) {

			this.providerType = providerType;

			this.configuredOptionsHolder.with(this.getClass().getAnnotation(Sorting.class).order());
			this.configuredOptionsHolder.with(this.getClass().getAnnotation(Sorting.class).direction());
			this.configuredOptionsHolder.with(Omitting.GLOBALFIELDS_NAME, this.getClass().getAnnotation(Omitting.class).globalFields());
		}

		@Override
		public Builder<T> with(SortingDirection sortingDirection) {
			this.configuredOptionsHolder.with(sortingDirection);
			return this;
		}

		@Override
		public Builder<T> with(SortingOrder sortingOrder) {
			this.configuredOptionsHolder.with(sortingOrder);
			return this;
		}

		@Override
		public Builder<T> with(String[] globalFields) {
			this.configuredOptionsHolder.with(Omitting.GLOBALFIELDS_NAME, globalFields);
			return this;
		}

		@Override
		public Builder<T> with(ConfiguredOptionsHolder configuredOptionsHolder) {
			this.configuredOptionsHolder = configuredOptionsHolder;
			return this;
		}

		public IL2Component build() throws InstantiationException, IllegalAccessException {

			SortingOptions sortingAnnotation = SortingOptions.class.newInstance();
			sortingAnnotation.setSortingDirection(this.configuredOptionsHolder.getValueFor(SortingDirection.class));
			sortingAnnotation.setSortingOrder(this.configuredOptionsHolder.getValueFor(SortingOrder.class));

			OmittingOptions omittingAnnotation = OmittingOptions.class.newInstance();
			omittingAnnotation.setGlobalFields((String[])this.configuredOptionsHolder.getValueFor(Omitting.GLOBALFIELDS_NAME));

			T providerTypeInstance = providerType.newInstance();
			providerTypeInstance.setSortingOption(sortingAnnotation);
			providerTypeInstance.setOmittingOption(omittingAnnotation);

			Instance<IL2Component> inst = CDI.current().select(IL2Component.class, providerTypeInstance);

			IL2Component beanInstance = inst.get();

			return beanInstance;
		}
	}

}
