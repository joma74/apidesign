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

    private final Map<String, Option> configuredOptions = new HashMap<>();

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public Option[] getConfiguration() {
        return this.configuredOptions.values().toArray(new Option[]{});
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
