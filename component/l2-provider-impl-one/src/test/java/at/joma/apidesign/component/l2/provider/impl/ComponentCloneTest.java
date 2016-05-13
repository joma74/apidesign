package at.joma.apidesign.component.l2.provider.impl;

import org.junit.Assert;
import org.junit.Test;

import at.joma.apidesign.component.l1.client.api.config.IConfiguration;
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;

public class ComponentCloneTest {

    @Test
    public void checkDeepClone() {

        ConfiguredOptionsHolder validOptions = new ConfiguredOptionsHolder();
        validOptions//
                .encloseFormatInfos(ComponentProducer.getFormatInfos())//
                .encloseFormatInfos(Component.getFormatInfos())//
                .with(SortingOrder.ALPHABETICALLY)//
                .with(SortingDirection.ASC)//
                .with(Omitting.BYFIELDNAMES_OPTIONNAME, new String[]{})//
                .with(Omitting.BYFIELDANNOTATIONS_OPTIONNAME, new Class[]{})//
                .with(Omitting.BYFIELDCLASSES_OPTIONNAME, new Class[]{})//
        ;

        Component c = new Component(validOptions);

        IConfiguration clonedConfig = c.deepClone();

        Assert.assertNotSame(validOptions, clonedConfig);

        Assert.assertEquals(validOptions, clonedConfig);
    }

}
