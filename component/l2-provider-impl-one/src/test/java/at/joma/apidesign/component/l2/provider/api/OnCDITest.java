package at.joma.apidesign.component.l2.provider.api;

import java.util.Arrays;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import org.apache.commons.collections4.ListUtils;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.provider.impl.Component;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;

@RunWith(CdiRunner.class)
@AdditionalClasses({
    ComponentProducer.class
})
public class OnCDITest {

    private static final Logger LOG = LoggerFactory.getLogger(OnCDITest.class);

    ExpectedException thrown;

    @Rule
    public ExpectedException getThrown() {
        this.thrown = ExpectedException.none();
        return this.thrown;
    }

    @Inject
    @AsXML
    IL1Component asXmlDefault;

    @Inject
    @AsXML
    IL1Component asXmlDefaultSame;

    @Inject
    @AsXML
    @Sorting(order = SortingOrder.GIVEN, direction = SortingDirection.NONE)
    @Omitting(globalFields = {
        "_parent"
    })
    IL1Component asXmlGiven;

    @Inject
    @AsXML
    @Sorting(order = SortingOrder.GIVEN, direction = SortingDirection.NONE)
    @Omitting(globalFields = {
        "_parent"
    })
    Instance<IL1Component> asXmlGivenInstance;

    @Inject
    @AsXML
    @Sorting(order = SortingOrder.GIVEN, direction = SortingDirection.ASC)
    @Omitting(globalFields = {
        "_parent"
    })
    Instance<IL1Component> asXmlWithInvalidOptions;

    @Rule
    public TestRule getWatchman() {
        return new TestWatcher() {

            @Override
            protected void starting(Description description) {
                LOG.info("*********** Running {} ***********", description.getMethodName());
            }
        };
    }

    @Test
    public void testDefaultSettings() {

        ConfiguredOptionsHolder configuredOptions_Expected = new ConfiguredOptionsHolder();
        configuredOptions_Expected//
                .encloseFormatInfos(ComponentProducer.getFormatInfos())//
                .encloseFormatInfos(Component.getFormatInfos())//
                .with(SortingOrder.ALPHABETICALLY)//
                .with(SortingDirection.ASC)//
                .with(Component.GLOBALFIELDS_OPTIONNAME, new String[]{})//
        ;

        Assert.assertNotNull(this.asXmlDefault);

        LOG.info(this.asXmlDefault.printConfiguration());

        Assert.assertEquals(configuredOptions_Expected.getFormatInfo(), asXmlDefault.getFormatInfo());

        Assert.assertEquals(3, asXmlDefault.optionsCount());

        Assert.assertTrue(ListUtils.isEqualList(Arrays.asList(configuredOptions_Expected.getOptions()), Arrays.asList(this.asXmlDefault.getOptions())));
    }

    @Test
    public void testForSameness() {

        Assert.assertSame(this.asXmlDefault, this.asXmlDefaultSame);
    }

    @Test
    public void testGivenSettings() {

        ConfiguredOptionsHolder configuredOptions_Expected = new ConfiguredOptionsHolder();
        configuredOptions_Expected//
                .encloseFormatInfos(ComponentProducer.getFormatInfos())//
                .encloseFormatInfos(Component.getFormatInfos())//
                .with(SortingOrder.GIVEN)//
                .with(SortingDirection.NONE)//
                .with(Component.GLOBALFIELDS_OPTIONNAME, new String[]{
                    "_parent"
                })//
        ;

        Assert.assertNotNull(this.asXmlGiven);

        LOG.info(this.asXmlGiven.printConfiguration());

        Assert.assertEquals(3, asXmlGiven.optionsCount());

        Assert.assertEquals(configuredOptions_Expected.getFormatInfo(), asXmlDefault.getFormatInfo());

        Assert.assertTrue(ListUtils.isEqualList(Arrays.asList(configuredOptions_Expected.getOptions()), Arrays.asList(this.asXmlGiven.getOptions())));
    }

    @Test
    public void testGivenSettingsWithInstance() {

        ConfiguredOptionsHolder configuredOptions_Expected = new ConfiguredOptionsHolder();
        configuredOptions_Expected//
                .encloseFormatInfos(ComponentProducer.getFormatInfos())//
                .encloseFormatInfos(Component.getFormatInfos())//
                .with(SortingOrder.GIVEN)//
                .with(SortingDirection.NONE)//
                .with(Component.GLOBALFIELDS_OPTIONNAME, new String[]{
                    "_parent"
                })//
        ;

        IL1Component theInstance = this.asXmlGivenInstance.get();

        Assert.assertNotNull(theInstance);

        LOG.info(theInstance.printConfiguration());

        Assert.assertEquals(3, theInstance.optionsCount());

        Assert.assertEquals(configuredOptions_Expected.getFormatInfo(), theInstance.getFormatInfo());

        Assert.assertTrue(ListUtils.isEqualList(Arrays.asList(configuredOptions_Expected.getOptions()), Arrays.asList(theInstance.getOptions())));
    }

    @Test
    public void testComponent_Given_InvalidConfiguration_Then_ConstraintViolationException() throws ReflectiveOperationException {

        this.thrown.expect(ConstraintViolationException.class);
        this.thrown.expectMessage(Component.ERROR_MESSAGE_OPTIONSNOTVALID);

        asXmlWithInvalidOptions.get();
    }

}
