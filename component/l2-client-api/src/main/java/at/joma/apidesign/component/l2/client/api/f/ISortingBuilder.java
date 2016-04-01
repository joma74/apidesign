package at.joma.apidesign.component.l2.client.api.f;

import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;

public interface ISortingBuilder {

    Builder with(SortingDirection direction);

    Builder with(SortingOrder order);

}
