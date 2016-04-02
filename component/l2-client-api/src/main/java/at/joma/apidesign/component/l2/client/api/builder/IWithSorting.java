package at.joma.apidesign.component.l2.client.api.builder;

import java.lang.annotation.Annotation;

import at.joma.apidesign.component.l2.client.api.IL2Component.Builder;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;

public interface IWithSorting<T extends IProviderRequiredOptions & Annotation> {

	Builder<T> with(SortingDirection direction);

	Builder<T> with(SortingOrder order);

}
