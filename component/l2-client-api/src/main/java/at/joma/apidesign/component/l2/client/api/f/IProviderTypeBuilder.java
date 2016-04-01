package at.joma.apidesign.component.l2.client.api.f;

import java.lang.annotation.Annotation;


public interface IProviderTypeBuilder<T extends IProviderTypeWithOptionsSetter & Annotation> {
    
	Builder<T> with(Class<T> providerType);

}
