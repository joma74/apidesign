package at.joma.apidesign.component.l2.client.api.f;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD })
public @interface Sorting {
	
	SortingOrder order() default SortingOrder.ALPHABETICALLY;
	
	SortingDirection direction() default SortingDirection.ASC;

}
