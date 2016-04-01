package at.joma.apidesign.component.l1.client.api;

import at.joma.apidesign.component.l1.client.api.types.ConfiguredOption;

public interface IL1Component {

	String serialize(Object serializable);

	ConfiguredOption[] getConfiguration();
	
	String printConfigurationOptions();

}
