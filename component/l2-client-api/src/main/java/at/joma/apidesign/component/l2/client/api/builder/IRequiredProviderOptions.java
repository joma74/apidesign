package at.joma.apidesign.component.l2.client.api.builder;

import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;

public interface IRequiredProviderOptions {

    void setOmittingOption(Omitting omitting);

    void setSortingOption(Sorting sorting);

}
