package at.joma.apidesign.component.l2.provider.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import at.joma.apidesign.component.l2.provider.api.impl.SelfValidatingValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.TYPE
})
@Constraint(validatedBy = SelfValidatingValidator.class)
@Documented
public @interface SelfValidating {

    String message() default "{at.joma.apidesign.component.l2.provider.api.SelfValidating.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
