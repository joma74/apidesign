package at.joma.apidesign.component.l2.client.api;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.builder.IProviderRequiredOptions;
import at.joma.apidesign.component.l2.client.api.builder.IWithOmitting;
import at.joma.apidesign.component.l2.client.api.builder.IWithSorting;
import at.joma.apidesign.component.l2.client.api.builder.options.OmittingOptions;
import at.joma.apidesign.component.l2.client.api.builder.options.SortingOptions;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;

public interface IL2Component extends IL1Component {
	
	@Sorting
	@Omitting
	public class Builder<T extends IProviderRequiredOptions & Annotation> implements IWithSorting<T>, IWithOmitting<T> {

		private SortingOrder sortingOrder = this.getClass().getAnnotation(Sorting.class).order();

		private SortingDirection sortingDirection = this.getClass().getAnnotation(Sorting.class).direction();

		private String[] globalFields = this.getClass().getAnnotation(Omitting.class).globalFields();

		private final Class<T> providerType;
		
		public Builder(Class<T> providerType) {
			
	        this.providerType = providerType;
	    }

		@Override
		public Builder<T> with(SortingDirection sortingDirection) {
			this.sortingDirection = sortingDirection;
			return this;
		}

		@Override
		public Builder<T> with(SortingOrder sortingOrder) {
			this.sortingOrder = sortingOrder;
			return this;
		}

		@Override
		public Builder<T> with(String[] globalFields) {
			this.globalFields = globalFields;
			return this;
		}

		public IL2Component build() throws InstantiationException, IllegalAccessException {

			SortingOptions sortingAnnotation = SortingOptions.class.newInstance();
			sortingAnnotation.setSortingDirection(Builder.this.sortingDirection);
			sortingAnnotation.setSortingOrder(Builder.this.sortingOrder);

			OmittingOptions omittingAnnotation = OmittingOptions.class.newInstance();
			omittingAnnotation.setGlobalFields(Builder.this.globalFields);

			T providerTypeInstance = providerType.newInstance();
			providerTypeInstance.setSortingOption(sortingAnnotation);
			providerTypeInstance.setOmittingOption(omittingAnnotation);

			Instance<IL2Component> inst = CDI.current().select(IL2Component.class, providerTypeInstance);

			IL2Component beanInstance = inst.get();

			return beanInstance;
		}
	}

}
