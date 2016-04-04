package at.joma.apidesign.component.l2.provider.api.builder;

import java.util.Arrays;

import org.apache.commons.collections4.ListUtils;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.IL2Component.Builder;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer.Configured;

@RunWith(CdiRunner.class)
@AdditionalClasses({
    ComponentProducer.class,
    Builder.class,
})
public class OnBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(OnBuilderTest.class);

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
    public void testComponent_ForSameness() throws ReflectiveOperationException {

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        configuredOptions//
                .with(SortingOrder.GIVEN)//
                .with(SortingDirection.DESC)//
                .with(Configured.GLOBALFIELDS_OPTIONNAME, new String[]{
                    "_parent"
                });

        Builder<AsXMLWithOptions> builder = new Builder<AsXMLWithOptions>(AsXMLWithOptions.class);

        IL1Component bean1 = builder//
                .with(configuredOptions.getValueFor(SortingOrder.class))//
                .with(configuredOptions.getValueFor(SortingDirection.class))//
                .with((String[]) configuredOptions.getValueFor(Configured.GLOBALFIELDS_OPTIONNAME))//
                .build();

        IL1Component bean2 = builder//
                .with(configuredOptions.getValueFor(SortingOrder.class))//
                .with(configuredOptions.getValueFor(SortingDirection.class))//
                .with((String[]) configuredOptions.getValueFor(Configured.GLOBALFIELDS_OPTIONNAME))//
                .build();

        Assert.assertSame(bean1, bean2);
    }

    @Test
    public void testComponent_With_Default() throws ReflectiveOperationException {

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        configuredOptions//
                .with(SortingOrder.ALPHABETICALLY)//
                .with(SortingDirection.ASC)//
                .with(Configured.GLOBALFIELDS_OPTIONNAME, new String[]{
                    ""
                });

        Builder<AsXMLWithOptions> builder = new Builder<AsXMLWithOptions>(AsXMLWithOptions.class);

        IL1Component bean = builder//
                .build();

        Assert.assertNotNull(bean);

        LOG.info(bean.printConfiguration());

        Assert.assertTrue(ListUtils.isEqualList(Arrays.asList(configuredOptions.getConfiguration()), Arrays.asList(bean.getConfiguration())));
    }

    @Test
    public void testComponent_With_GivenDescParent() throws ReflectiveOperationException {

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        configuredOptions//
                .with(SortingOrder.GIVEN)//
                .with(SortingDirection.DESC)//
                .with(Configured.GLOBALFIELDS_OPTIONNAME, new String[]{
                    "_parent"
                });

        Builder<AsXMLWithOptions> builder = new Builder<AsXMLWithOptions>(AsXMLWithOptions.class);

        IL1Component bean = builder//
                .with(configuredOptions.getValueFor(SortingOrder.class))//
                .with(configuredOptions.getValueFor(SortingDirection.class))//
                .with((String[]) configuredOptions.getValueFor(Configured.GLOBALFIELDS_OPTIONNAME))//
                .build();

        Assert.assertNotNull(bean);

        LOG.info(bean.printConfiguration());

        Assert.assertTrue(ListUtils.isEqualList(Arrays.asList(configuredOptions.getConfiguration()), Arrays.asList(bean.getConfiguration())));
    }

}
