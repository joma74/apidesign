package at.joma.apidesign.component.l2.client.api.types.config;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.joma.apidesign.component.l1.client.api.config.IOption;

import com.google.common.base.Preconditions;

public class Option implements IOption, Serializable {

    private static final long serialVersionUID = 5979614171013169677L;

    public enum OptionType {
        ENUM(Enum.class),
        STRINGARRAY(String[].class),
        STRING(String.class);

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
        Preconditions.checkNotNull(option);
        this.name = option.getClass().getSimpleName();
        this.value = option.name();
        this.type = OptionType.ENUM;
    }

    public Option(String optionName, String[] optionValue) {
        Preconditions.checkNotNull(optionName);
        Preconditions.checkNotNull(optionValue);
        this.name = optionName;
        this.value = Arrays.toString(optionValue);
        this.type = OptionType.STRINGARRAY;
    }

    public Option(String optionName, String optionValue) {
        Preconditions.checkNotNull(optionName);
        Preconditions.checkNotNull(optionValue);
        this.name = optionName;
        this.value = optionValue;
        this.type = OptionType.STRING;
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
    public String toString() {
        return this.getClass().getSimpleName() + "[" + getName() + ", " + getValue() + "]";
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
