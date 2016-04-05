package at.joma.apidesign.component.l2.client.api.types.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.config.IConfiguration;
import at.joma.apidesign.component.l2.client.api.types.config.Option.OptionType;

public class ConfiguredOptionsHolder implements IConfiguration, Serializable {

	private static final long serialVersionUID = 7863290157676465486L;
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfiguredOptionsHolder.class);

	public static final String MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR = "No configured option for ";

	private static final String TAB = "\t";

	private final Map<String, Option> configuredOptions = new HashMap<>();

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public Option[] getConfiguration() {
		return this.configuredOptions.values().toArray(new Option[] {});
	}

	public <T extends Enum<T>> T getValueFor(Class<T> enumType) {
		Option conf = this.configuredOptions.get(enumType.getSimpleName());
		if (conf != null) {
			return Enum.valueOf(enumType, conf.value);
		}
		throw new IllegalArgumentException(MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + enumType.getSimpleName());
	}

	public Object getValueFor(String optionName) {
		Option conf = this.configuredOptions.get(optionName);
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
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public int optionsCount() {
		return this.configuredOptions.size();
	}

	@Override
	public String printConfiguration() {
		StringBuilder configuration = new StringBuilder(System.lineSeparator() + "Configuration" + System.lineSeparator());
		for (Option option : this.configuredOptions.values()) {
			configuration.append(TAB + option.name + "/" + option.type.getType().getSimpleName() + ":" + option.value + System.lineSeparator());
		}
		return configuration.toString();
	}

	@Override
	public IConfiguration deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (IConfiguration) ois.readObject();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return null;
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public <T extends Enum<T>> ConfiguredOptionsHolder with(Enum<T> option) {
		Option conf = new Option(option);
		this.configuredOptions.put(conf.name, conf);
		return this;
	}

	public ConfiguredOptionsHolder with(String optionName, String[] optionValue) {
		Option conf = new Option(optionName, optionValue);
		this.configuredOptions.put(optionName, conf);
		return this;
	}

}
