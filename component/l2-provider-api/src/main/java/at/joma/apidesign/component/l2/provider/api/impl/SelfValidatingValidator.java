package at.joma.apidesign.component.l2.provider.api.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import at.joma.apidesign.component.l1.client.api.IValidatable;
import at.joma.apidesign.component.l2.provider.api.SelfValidating;

public class SelfValidatingValidator implements ConstraintValidator<SelfValidating, IValidatable> {

    public void initialize(SelfValidating constraintAnnotation) {
    }

    public boolean isValid(IValidatable value, ConstraintValidatorContext constraintValidatorContext) {

        return value.isValid();
    }
}
