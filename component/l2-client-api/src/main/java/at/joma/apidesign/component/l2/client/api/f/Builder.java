package at.joma.apidesign.component.l2.client.api.f;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.IL2Component.OmittingAnnotation;
import at.joma.apidesign.component.l2.client.api.IL2Component.SortingAnnotation;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;

public class Builder<T extends IProviderTypeWithOptionsSetter & Annotation> implements ISortingBuilder, IOmittingBuilder, IProviderTypeBuilder<T> {

	private SortingOrder sortingOrder = SortingOrder.ALPHABETICALLY;

	private SortingDirection sortingDirection = SortingDirection.ASC;

	private String[] globalFields = new String[] {};

	private Class<T> providerType = null;

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

	@Override
	public Builder<T> with(Class<T> providerType) {
		this.providerType = providerType;
		return this;
	}

	public IL2Component build() throws InstantiationException, IllegalAccessException {

		SortingAnnotation sortingAnnotation = new SortingAnnotation() {

			private static final long serialVersionUID = 3395353165326173089L;

			@Override
			public SortingOrder order() {
				return Builder.this.sortingOrder;
			}

			@Override
			public SortingDirection direction() {
				return Builder.this.sortingDirection;
			}
		};

		OmittingAnnotation omittingAnnotation = new OmittingAnnotation() {

			private static final long serialVersionUID = -1139593265279393709L;

			@Override
			public String[] globalFields() {
				return Builder.this.globalFields;
			}
		};

		T providerTypeWithConfig = providerType.newInstance();
		providerTypeWithConfig.setSorting(sortingAnnotation);
		providerTypeWithConfig.setOmitting(omittingAnnotation);

		Instance<IL2Component> inst = CDI.current().select(IL2Component.class, providerTypeWithConfig);

		IL2Component beanInstance = inst.get();

		return beanInstance;
	}
}
