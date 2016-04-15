package at.joma.apidesign.component.l1.client.api.config;

import java.util.Map;

public interface IConfiguration {

    public static final String FORMATINFO_KEY_COMPONENT = "Component";
	public static final String FORMATINFO_KEY_FORMAT = "Format";
	public static final String FORMATINFO_KEY_PRODUCER = "Producer";
	public static final String FORMATINFO_KEY_FORMATTER = "Formatter";

	Map<String, String> getFormatInfo();

	IOption[] getOptions();

	int optionsCount();

	String printConfiguration();

	IConfiguration deepClone();

}
