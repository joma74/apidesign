package at.joma.apidesign.component.l2.client.api.f;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;

import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.TYPE,
    ElementType.METHOD
})
public @interface Sorting {

    public static final String ORDER_METHODNAME = "order";
    public static final String DIRECTION_METHODNAME = "direction";

    @Nonbinding
    SortingOrder order() default SortingOrder.ALPHABETICALLY;


    @Nonbinding
    SortingDirection direction() default SortingDirection.ASC;

}
