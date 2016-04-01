package at.joma.apidesign.component.l2.client.api.f;

import java.lang.annotation.Annotation;

import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;

public interface ISortingBuilder<T extends IProviderTypeWithOptionsSetter & Annotation> {

	Builder<T> with(SortingDirection direction);

	Builder<T> with(SortingOrder order);

}
