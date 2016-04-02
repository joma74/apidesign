package at.joma.apidesign.component.l2.client.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;

import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.TYPE,
    ElementType.METHOD
})
public @interface Sorting {

    @Nonbinding
    SortingOrder order() default SortingOrder.ALPHABETICALLY;


    @Nonbinding
    SortingDirection direction() default SortingDirection.ASC;

}
