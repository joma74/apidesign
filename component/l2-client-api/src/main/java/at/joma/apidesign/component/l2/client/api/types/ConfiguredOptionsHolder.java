package at.joma.apidesign.component.l2.client.api.types;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.joma.apidesign.component.l1.client.api.IConfigured;
import at.joma.apidesign.component.l2.client.api.types.ConfiguredOption.OptionType;

public class ConfiguredOptionsHolder implements IConfigured{
	
	public static final String MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR = "No configured option for ";

	private static final String TAB = "\t";

	private CopyOnWriteArraySet<ConfiguredOption> configuredOptions = new CopyOnWriteArraySet<ConfiguredOption>();

	public ConfiguredOptionsHolder with(Enum<?> option) {
		ConfiguredOption conf = new ConfiguredOption(option);
		configuredOptions.add(conf);
		return this;
	}

	public ConfiguredOptionsHolder with(String optionName, String[] optionValue) {
		ConfiguredOption conf = new ConfiguredOption(optionName, optionValue);
		configuredOptions.add(conf);
		return this;
	}

	public <T extends Enum<T>> T getValueFor(Class<T> enumType) {
		Iterator<ConfiguredOption> iterator = configuredOptions.iterator();
		while (iterator.hasNext()) {
			ConfiguredOption conf = iterator.next();
			if (conf.type == OptionType.ENUM && enumType.getSimpleName().equals(conf.name)) {
				return Enum.valueOf(enumType, conf.value);
			}
		}
		throw new IllegalArgumentException(MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + enumType.getSimpleName());
	}

	public Object getValueFor(String optionName) {
		Iterator<ConfiguredOption> iterator = configuredOptions.iterator();
		while (iterator.hasNext()) {
			ConfiguredOption conf = iterator.next();
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
		return (ConfiguredOption[]) configuredOptions.toArray(new ConfiguredOption[]{});
	}
	
	@Override
	public String printConfiguration() {
		StringBuilder configuration = new StringBuilder(System.lineSeparator() + "Configuration" + System.lineSeparator());
		for (ConfiguredOption option : configuredOptions) {
			configuration.append(TAB + option.name + "/" + option.type.getType().getSimpleName() + ":" + option.value + System.lineSeparator());
		}
		return configuration.toString();
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
