package at.joma.apidesign.component.l2.provider.api.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import at.joma.apidesign.component.l2.client.api.f.Omitting;
import at.joma.apidesign.component.l2.client.api.f.Sorting;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD,
    ElementType.TYPE,
    ElementType.METHOD
})
public @interface AsXMLByBuilder {

    @Nonbinding
    public Sorting sorting() default @Sorting;

    @Nonbinding
    public Omitting ommiting() default @Omitting;

}
