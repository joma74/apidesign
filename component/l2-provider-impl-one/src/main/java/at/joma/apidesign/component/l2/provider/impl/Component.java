package at.joma.apidesign.component.l2.provider.impl;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.joma.apidesign.component.l1.client.api.config.IConfiguration;
import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.client.api.types.config.Option;
import at.joma.apidesign.component.l2.provider.api.SelfValidating;

@SelfValidating
public class Component implements IL2Component {

    public static final String GLOBALFIELDS_OPTIONNAME = "globalFields";

    public static final String FORMATINFO_KEY_FORMATTER = "XStream";

    protected static final Map<String, String> FORMATINFOS = new HashMap<>();

    static {
        FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_FORMATTER, FORMATINFO_KEY_FORMATTER);
        FORMATINFOS.put(IConfiguration.FORMATINFO_KEY_COMPONENT, Component.class.getName());
    }

    private WeakReference<ConfiguredOptionsHolder> configuredOptions = new WeakReference<ConfiguredOptionsHolder>(new ConfiguredOptionsHolder());

    public Component(ConfiguredOptionsHolder configuredOptions) {
        encloseFormatInfo(configuredOptions);
        this.configuredOptions = new WeakReference<ConfiguredOptionsHolder>(configuredOptions);
    }

    public Component(Map<String, String> formatInfos, SortingOrder orderOption, SortingDirection directionOption, String[] globalFieldsOption) {
        this.configuredOptions.get()//
                .encloseFormatInfos(formatInfos)//
                .with(orderOption)//
                .with(directionOption)//
                .with(GLOBALFIELDS_OPTIONNAME, globalFieldsOption)//
        ;
        encloseFormatInfo(this.configuredOptions.get());
    }

    private void encloseFormatInfo(ConfiguredOptionsHolder configuredOptions) {
        configuredOptions//
                .encloseFormatInfos(FORMATINFOS)//
        ;
    }

    public static Map<String, String> getFormatInfos() {
        return Collections.unmodifiableMap(FORMATINFOS);
    }

    @Override
    public Option[] getOptions() {
        return this.configuredOptions.get().getOptions();
    }

    @Override
    public Map<String, String> getFormatInfo() {
        return this.configuredOptions.get().getFormatInfo();
    }

    @Override
    public int optionsCount() {
        return this.configuredOptions.get().optionsCount();
    }

    @Override
    public String printConfiguration() {
        return this.configuredOptions.get().printConfiguration();
    }

    @Override
    public String serialize(Object serializable) {
        return null;
    }

    @Override
    public IConfiguration deepClone() {
        return this.configuredOptions.get().deepClone();
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
