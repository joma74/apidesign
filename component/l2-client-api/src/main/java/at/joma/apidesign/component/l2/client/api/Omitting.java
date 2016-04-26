package at.joma.apidesign.component.l2.client.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.TYPE,
    ElementType.METHOD
})
public @interface Omitting {

    public static final String BYFIELDNAMES_OPTIONNAME = "globalFields";

    @Nonbinding
    String[] byFieldNames() default {
        ""
    };
}
