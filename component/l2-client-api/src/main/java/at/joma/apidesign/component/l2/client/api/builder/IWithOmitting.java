package at.joma.apidesign.component.l2.client.api.builder;

import java.lang.annotation.Annotation;

import at.joma.apidesign.component.l2.client.api.IL2Component.Builder;

public interface IWithOmitting<T extends IRequiredProviderOptions & Annotation> {

    Builder<T> with(String[] globalFields);

}
