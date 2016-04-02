package at.joma.apidesign.component.l2.client.api.builder;

import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;

public interface IProviderRequiredOptions {
	
	void setSortingOption(Sorting sorting);
	
	void setOmittingOption(Omitting omitting);

}
