package at.joma.apidesign.component.l2.provider.impl;

import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

public class ValidOptionsTuple {
    
    public static final Attribute<ValidOptionsTuple, SortingOrder> VOT_SORTINGORDER = new SimpleAttribute<ValidOptionsTuple, SortingOrder>("sortingOrder") {
        public SortingOrder getValue(ValidOptionsTuple ruleTuple, QueryOptions queryOptions) { return ruleTuple.sortingOrder; }
    };
    
    public static final Attribute<ValidOptionsTuple, SortingDirection> VOT_SORTINGDIRECTION = new SimpleAttribute<ValidOptionsTuple, SortingDirection>("sortingDirection") {
        public SortingDirection getValue(ValidOptionsTuple ruleTuple, QueryOptions queryOptions) { return ruleTuple.sortingDirection; }
    };

    public ValidOptionsTuple(SortingOrder sortingOrder, SortingDirection sortingDirection) {
        this.sortingOrder = sortingOrder;
        this.sortingDirection = sortingDirection;
    }

    public SortingOrder sortingOrder;

    public SortingDirection sortingDirection;

}
