package at.joma.apidesign.component.l2.client.api.types.config;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.joma.apidesign.component.l1.client.api.config.IOption;

public class Option implements IOption {

    public enum OptionType {
        ENUM(Enum.class),
        STRINGARRAY(String[].class);

        final Class<?> type;

        OptionType(final Class<?> type) {
            this.type = type;
        }

        public Class<?> getType() {
            return this.type;
        }
    };

    public final String name;

    public final String value;

    public final OptionType type;

    public Option(Enum<?> option) {
        this.name = option.getClass().getSimpleName();
        this.value = option.name();
        this.type = OptionType.ENUM;
    }

    public Option(String optionName, String[] optionValue) {
        this.name = optionName;
        this.value = Arrays.toString(optionValue);
        this.type = OptionType.STRINGARRAY;
    }

    public String[] convertValueToArray() {

        String joinedMinusBrackets = this.value.substring(1, this.value.length() - 1);

        return joinedMinusBrackets.split(", ");

    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public OptionType getType() {
        return this.type;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
