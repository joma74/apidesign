package at.joma.apidesign.component.l2.client.api.types.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.config.IConfiguration;
import at.joma.apidesign.component.l2.client.api.types.config.Option.OptionType;

import com.google.common.base.Objects;

public class ConfiguredOptionsHolder implements IConfiguration, Serializable {

    private static final long serialVersionUID = 7863290157676465486L;

    private static final Logger LOG = LoggerFactory.getLogger(ConfiguredOptionsHolder.class);

    public static final String MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR = "No configured option for ";

    public static final String MESSAGE_FAILURE_ASKCONFIGIUREDENUMOPTIONBYENUM =
            "The configured option value for your type enum must be asked via getValueFor(Class<T> enumType) only. Applies for ";

    private static final String TAB = "\t";

    private final Map<String, Option> configuredOptions = new TreeMap<>();

    private final Map<String, String> formatInfos = new TreeMap<>();

    @Override
    public Option[] getOptions() {
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
                throw new IllegalArgumentException(MESSAGE_FAILURE_ASKCONFIGIUREDENUMOPTIONBYENUM + optionName);
            }
        }
        throw new IllegalArgumentException(MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + optionName);
    }

    @Override
    public int optionsCount() {
        return this.configuredOptions.size();
    }

    @Override
    public String printConfiguration() {
        StringBuilder configuration = new StringBuilder(System.lineSeparator());
        configuration.append("Configuration" + System.lineSeparator());
        for (Entry<String, String> formatInfo : formatInfos.entrySet()) {
            configuration.append(TAB + formatInfo.getKey() + ":" + formatInfo.getValue() + System.lineSeparator());
        }
        configuration.append(TAB + "Options" + System.lineSeparator());
        for (Option option : this.configuredOptions.values()) {
            configuration.append(TAB + TAB + option.name + "/" + option.type.getType().getSimpleName() + ":" + option.value + System.lineSeparator());
        }
        return configuration.toString();
    }

    @Override
    public Map<String, String> getFormatInfo() {

        return Collections.unmodifiableMap(formatInfos);
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
        } catch (IOException | ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
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

    public ConfiguredOptionsHolder encloseFormatInfos(Map<String, String> formatInfos) {
        this.formatInfos.putAll(formatInfos);
        return this;
    }

    public ConfiguredOptionsHolder encloseFormatInfo(String formatInfoKey, String formatInfoValue) {
        this.formatInfos.put(formatInfoKey, formatInfoValue);
        return this;
    }

    @Override
    public int hashCode() {
         return Objects.hashCode(configuredOptions, formatInfos);
    }

    @Override
    public boolean equals(Object obj) {
         if (obj instanceof ConfiguredOptionsHolder) {
         final ConfiguredOptionsHolder other = (ConfiguredOptionsHolder) obj;
         return Objects.equal(configuredOptions, other.configuredOptions) &&
         Objects.equal(formatInfos, other.formatInfos);
         } else {
         return false;
         }
    }
}
