package at.joma.apidesign.component.l2.client.api.types.config;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.joma.apidesign.component.l1.client.api.config.IOption;

import com.google.common.base.Preconditions;

public class Option implements IOption, Serializable {

    private static final long serialVersionUID = 5979614171013169677L;

    public enum OptionType {
        ENUM(Enum.class),
        STRINGARRAY(String[].class),
        STRING(String.class),
        CLAZZARRAY(Class[].class);

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

    public Option(String optionName, String[] optionValues) {
        Preconditions.checkNotNull(optionName);
        Preconditions.checkNotNull(optionValues);
        this.name = optionName;
        this.value = Arrays.toString(optionValues);
        this.type = OptionType.STRINGARRAY;
    }

    public Option(String optionName, String optionValue) {
        Preconditions.checkNotNull(optionName);
        Preconditions.checkNotNull(optionValue);
        this.name = optionName;
        this.value = optionValue;
        this.type = OptionType.STRING;
    }
    
    public Option(String optionName, Class<?>[] optionValues) {
        Preconditions.checkNotNull(optionName);
        Preconditions.checkNotNull(optionValues);
        this.name = optionName;
        String[] intermediate = new String[optionValues.length];
        for(Class<?> optionValue : optionValues){
        	ArrayUtils.add(intermediate, optionValue.getName());
        }
        this.value = Arrays.toString(intermediate);
        this.type = OptionType.CLAZZARRAY;
    }

    public String[] convertValueToArray() {
        String joinedMinusBrackets = this.value.substring(1, this.value.length() - 1);
        return joinedMinusBrackets.split(", ");
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
    public String toString() {
        return this.getClass().getSimpleName() + "[" + getName() + ", " + getValue() + "]";
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
