package at.joma.apidesign.component.l1.client.api;

import at.joma.apidesign.component.l1.client.api.config.IConfiguration;

public interface IL1Component extends IConfiguration, IValidatable {

    String serialize(Object serializable);

}
