package at.joma.apidesign.component.l1.client.api;

public interface IConfigured {

	IConfiguredOption[] getConfiguration();

	String printConfiguration();
	
	int size();

}