package at.joma.apidesign.component.l2.provider.impl;

import org.junit.Assert;
import org.junit.Test;

import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;

public class ComponentRulesTableTest {

    @Test
    public void checkAValidConfig() {

        ConfiguredOptionsHolder validOptions = new ConfiguredOptionsHolder();
        validOptions//
                .encloseFormatInfos(ComponentProducer.getFormatInfos())//
                .encloseFormatInfos(Component.getFormatInfos())//
                .with(SortingOrder.ALPHABETICALLY)//
                .with(SortingDirection.ASC)//
                .with(Component.GLOBALFIELDS_OPTIONNAME, new String[]{})//
        ;

        Component c = new Component(validOptions);

        Assert.assertTrue(c.isValid());
    }
    
    @Test
    public void checkANotValidConfig_1() {

        ConfiguredOptionsHolder invalidOptions = new ConfiguredOptionsHolder();
        invalidOptions//
                .encloseFormatInfos(ComponentProducer.getFormatInfos())//
                .encloseFormatInfos(Component.getFormatInfos())//
                .with(SortingOrder.GIVEN)//
                .with(SortingDirection.ASC)//
                .with(Component.GLOBALFIELDS_OPTIONNAME, new String[]{})//
        ;

        Component c = new Component(invalidOptions);

        Assert.assertFalse(c.isValid());
    }
    
    @Test
    public void checkANotValidConfig_2() {

        ConfiguredOptionsHolder invalidOptions = new ConfiguredOptionsHolder();
        invalidOptions//
                .encloseFormatInfos(ComponentProducer.getFormatInfos())//
                .encloseFormatInfos(Component.getFormatInfos())//
                .with(SortingOrder.ALPHABETICALLY)//
                .with(SortingDirection.NONE)//
                .with(Component.GLOBALFIELDS_OPTIONNAME, new String[]{})//
        ;

        Component c = new Component(invalidOptions);

        Assert.assertFalse(c.isValid());
    }

}
