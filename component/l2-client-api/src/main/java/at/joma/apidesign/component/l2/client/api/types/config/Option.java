package at.joma.apidesign.component.l2.client.api.types.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import at.joma.apidesign.component.l1.client.api.config.IOption;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

public class Option implements IOption, Serializable {

    private static final long serialVersionUID = 5979614171013169677L;

    private static final Logger LOG = LoggerFactory.getLogger(Option.class);

    public static final String ERR_OPTIONVALUE_NULL = "optionValue may not be null. optionName was >>{}<<.)";
    
    public static final String ERR_OPTIONVALUES_NULL = "optionValues of >>{}<< may not contain null values. optionName was >>{}<<.";
    
    public static final String ERR_OPTIONNAME_NULL = "optionName may not be null";
    

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
        checkOptionPreconditions(optionName, optionValues);
        this.name = optionName;
        this.value = Arrays.toString(optionValues);
        this.type = OptionType.STRINGARRAY;
    }

    public Option(String optionName, String optionValue) {
        checkOptionPreconditions(optionName, optionValue);
        this.name = optionName;
        this.value = optionValue;
        this.type = OptionType.STRING;
    }

    public Option(String optionName, Class<?>[] optionValues) {
        checkOptionPreconditions(optionName, optionValues);
        this.name = optionName;
        Set<String> intermediate = new HashSet<>(optionValues.length);
        for (Class<?> optionValue : optionValues) {
            intermediate.add(optionValue.getName());
        }
        this.value = Arrays.toString(intermediate.toArray());
        this.type = OptionType.CLAZZARRAY;
    }

    private void checkOptionPreconditions(String optionName, Object optionValue) {
        Preconditions.checkNotNull(ERR_OPTIONNAME_NULL, optionName);
        Preconditions.checkNotNull(MessageFormatter.format(ERR_OPTIONVALUE_NULL, optionName).getMessage(), optionValue);
    }

    private void checkOptionPreconditions(String optionName, Object[] optionValues) {
        Preconditions.checkNotNull(ERR_OPTIONNAME_NULL, optionName);
        Preconditions.checkNotNull(optionValues);
        if (!Collections2.filter(Arrays.asList(optionValues), Predicates.isNull()).isEmpty()) {
            throw new NullPointerException(MessageFormatter.format(ERR_OPTIONVALUES_NULL, Arrays.toString(optionValues), optionName).getMessage());
        }
    }

    public String[] convertValueToArray() {
        String joinedMinusBrackets = this.value.substring(1, this.value.length() - 1);
        if (joinedMinusBrackets.isEmpty()) {
            return new String[]{};
        }
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
    public IOption deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (IOption) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
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
