package at.joma.apidesign.component.l2.client.api.types.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.joma.apidesign.component.l1.client.api.config.IConfiguration;
import at.joma.apidesign.component.l2.client.api.types.config.Option.OptionType;

public class ConfiguredOptionsHolder implements IConfiguration {

	public static final String MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR = "No configured option for ";

	private static final String TAB = "\t";

	private Map<String, Option> configuredOptions = new HashMap<String, Option>();

	public <T extends Enum<T>> ConfiguredOptionsHolder with(Enum<T> option) {
		Option conf = new Option(option);
		configuredOptions.put(conf.name, conf);
		return this;
	}

	public ConfiguredOptionsHolder with(String optionName, String[] optionValue) {
		try {
			Object found = getValueFor(optionName);
			if (found != null) {
				configuredOptions.remove(found);
			}
		} catch (IllegalArgumentException iae) {
			// NOP
		}
		Option conf = new Option(optionName, optionValue);
		configuredOptions.put(optionName, conf);
		return this;
	}

	public <T extends Enum<T>> T getValueFor(Class<T> enumType) {
		Option conf = configuredOptions.get(enumType.getSimpleName());
		if (conf != null) {
			return Enum.valueOf(enumType, conf.value);
		}
		throw new IllegalArgumentException(MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + enumType.getSimpleName());
	}

	public Object getValueFor(String optionName) {
		Option conf = configuredOptions.get(optionName);
		if (conf != null) {
			if (conf.type == OptionType.STRINGARRAY && optionName.equals(conf.name)) {
				return conf.convertValueToArray();
			}
			if (conf.type == OptionType.ENUM && optionName.equals(conf.name)) {
				return conf.value;
			}
		}
		throw new IllegalArgumentException(MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + optionName);
	}

	@Override
	public Option[] getConfiguration() {
		return (Option[]) configuredOptions.values().toArray(new Option[] {});
	}

	@Override
	public String printConfiguration() {
		StringBuilder configuration = new StringBuilder(System.lineSeparator() + "Configuration" + System.lineSeparator());
		for (Option option : configuredOptions.values()) {
			configuration.append(TAB + option.name + "/" + option.type.getType().getSimpleName() + ":" + option.value + System.lineSeparator());
		}
		return configuration.toString();
	}

	@Override
	public int optionsCount() {
		return configuredOptions.size();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
