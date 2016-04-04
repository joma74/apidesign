package at.joma.apidesign.component.l1.client.api.config;

public interface IConfiguration {

	IOption[] getConfiguration();

	String printConfiguration();
	
	int optionsCount();

}