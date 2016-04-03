package at.joma.apidesign.component.l2.client.api.builder;

import java.lang.annotation.Annotation;

import at.joma.apidesign.component.l2.client.api.IL2Component.Builder;
import at.joma.apidesign.component.l2.client.api.types.ConfiguredOptionsHolder;

public interface IWithConfiguredOptionsHolder<T extends IProviderRequiredOptions & Annotation> {
	
	Builder<T> with(ConfiguredOptionsHolder configuredOptionsHolder);

}
