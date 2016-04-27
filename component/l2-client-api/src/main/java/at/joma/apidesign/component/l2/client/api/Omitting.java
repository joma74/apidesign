package at.joma.apidesign.component.l2.client.api;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Inject;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD })
public @interface Omitting {

	public static final String BYFIELDNAMES_OPTIONNAME = "omitByFieldNames";

	@Nonbinding
	String[] byFieldNames() default { "" };
	
	public static final String BYFIELDANNOTATIONS_OPTIONNAME = "omitByFieldAnnotations";

	@Nonbinding
	Class<? extends Annotation>[] byFieldAnnotations() default { Inject.class };
	
	public static final String BYFIELDCLASSES_OPTIONNAME = "omitByFieldClasses";

	@Nonbinding
	Class<?>[] byFieldClasses() default {

	};
}
