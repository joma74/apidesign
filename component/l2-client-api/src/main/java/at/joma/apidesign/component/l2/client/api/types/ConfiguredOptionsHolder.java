package at.joma.apidesign.component.l2.client.api.types;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.joma.apidesign.component.l1.client.api.IConfigured;
import at.joma.apidesign.component.l2.client.api.types.ConfiguredOption.OptionType;

public class ConfiguredOptionsHolder implements IConfigured {

	public static final String MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR = "No configured option for ";

	private static final String TAB = "\t";

	private Map<String, ConfiguredOption> configuredOptions = new HashMap<String, ConfiguredOption>();

	public <T extends Enum<T>> ConfiguredOptionsHolder with(Enum<T> option) {
		ConfiguredOption conf = new ConfiguredOption(option);
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
		ConfiguredOption conf = new ConfiguredOption(optionName, optionValue);
		configuredOptions.put(optionName, conf);
		return this;
	}

	public <T extends Enum<T>> T getValueFor(Class<T> enumType) {
		ConfiguredOption conf = configuredOptions.get(enumType.getSimpleName());
		if (conf != null) {
			return Enum.valueOf(enumType, conf.value);
		}
		throw new IllegalArgumentException(MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + enumType.getSimpleName());
	}

	public Object getValueFor(String optionName) {
		ConfiguredOption conf = configuredOptions.get(optionName);
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
	public ConfiguredOption[] getConfiguration() {
		return (ConfiguredOption[]) configuredOptions.values().toArray(new ConfiguredOption[] {});
	}

	@Override
	public String printConfiguration() {
		StringBuilder configuration = new StringBuilder(System.lineSeparator() + "Configuration" + System.lineSeparator());
		for (ConfiguredOption option : configuredOptions.values()) {
			configuration.append(TAB + option.name + "/" + option.type.getType().getSimpleName() + ":" + option.value + System.lineSeparator());
		}
		return configuration.toString();
	}

	@Override
	public int size() {
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
