package at.joma.apidesign.component.l2.provider.impl;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.builder.options.OmittingOptions;
import at.joma.apidesign.component.l2.client.api.builder.options.SortingOptions;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.provider.impl.out1.OUT_OmitByClassTest;
import at.joma.apidesign.component.l2.provider.impl.out1.OUT_Ser_1;

public class ComponentTest {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentTest.class);

    @Rule
    public TestRule getWatchman() {
        return new TestWatcher() {

            @Override
            protected void starting(Description description) {
                LOG.info("*********** Running {} ***********", description.getMethodName());
            }
        };
    }

    private static final String XMLSERIALIZED_OUT_EXPECTED_1 = ""//
            + "<at.joma.apidesign.component.l2.provider.impl.out1.OUT__Ser__1>\n"//
            + "  <c>\n"//
            + "    <d>d</d>\n"//
            + "  </c>\n"//
            + "  <e>e</e>\n"//
            + "</at.joma.apidesign.component.l2.provider.impl.out1.OUT__Ser__1>";

    private static final String XMLSERIALIZED_OUT_EXPECTED_2 = ""//
            + "<at.joma.apidesign.component.l2.provider.impl.out1.OUT__Ser__1>\n"//
            + "  <e>e</e>\n"//
            + "  <c>\n"//
            + "    <d>d</d>\n"//
            + "  </c>\n"//
            + "</at.joma.apidesign.component.l2.provider.impl.out1.OUT__Ser__1>";

    private static final String XMLSERIALIZED_OUT_EXPECTED_GIVEN = ""//
            + "<at.joma.apidesign.component.l2.provider.impl.out1.OUT__Ser__1>\n"//
            + "  <__parent>_parent</__parent>\n"//
            + "  <c>\n"//
            + "    <d>d</d>\n"//
            + "  </c>\n"//
            + "  <b/>\n"//
            + "  <e>e</e>\n" + "</at.joma.apidesign.component.l2.provider.impl.out1.OUT__Ser__1>";

    private static final String XMLSERIALIZED_OUT_EXPECTED_DEFAULT = ""//
            + "<at.joma.apidesign.component.l2.provider.impl.out1.OUT__Ser__1>\n"//
            + "  <__parent>_parent</__parent>\n"//
            + "  <b/>\n"//
            + "  <c>\n"//
            + "    <d>d</d>\n"//
            + "  </c>\n"//
            + "  <e>e</e>\n"//
            + "</at.joma.apidesign.component.l2.provider.impl.out1.OUT__Ser__1>";

    @Test
    public void testSerialize_1() {

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        configuredOptions.with(SortingDirection.ASC);
        configuredOptions.with(SortingOrder.ALPHABETICALLY);
        configuredOptions.with(Omitting.BYFIELDNAMES_OPTIONNAME, new String[]{
            "_parent"
        });
        configuredOptions.with(Omitting.BYFIELDANNOTATIONS_OPTIONNAME, new Class[]{
            Inject.class
        });
        configuredOptions.with(Omitting.BYFIELDCLASSES_OPTIONNAME, new Class[]{
            OUT_OmitByClassTest.class
        });
        Component configured = new Component(configuredOptions);

        Assert.assertTrue(configured.isValid());
        configured.initializeSerializingInstance();

        OUT_Ser_1 out_Ser_1 = new OUT_Ser_1();

        Assert.assertEquals(XMLSERIALIZED_OUT_EXPECTED_1, configured.serialize(out_Ser_1));
    }

    @Test
    public void testSerialize_2() {

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        configuredOptions.with(SortingDirection.DESC);
        configuredOptions.with(SortingOrder.ALPHABETICALLY);
        configuredOptions.with(Omitting.BYFIELDNAMES_OPTIONNAME, new String[]{
            "_parent"
        });
        configuredOptions.with(Omitting.BYFIELDANNOTATIONS_OPTIONNAME, new Class[]{
            Inject.class
        });
        configuredOptions.with(Omitting.BYFIELDCLASSES_OPTIONNAME, new Class[]{
            OUT_OmitByClassTest.class
        });
        Component configured = new Component(configuredOptions);

        Assert.assertTrue(configured.isValid());
        configured.initializeSerializingInstance();

        OUT_Ser_1 out_Ser_1 = new OUT_Ser_1();

        Assert.assertEquals(XMLSERIALIZED_OUT_EXPECTED_2, configured.serialize(out_Ser_1));
    }

    @Test
    public void testSerialize_Given() {

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        configuredOptions.with(SortingDirection.NONE);
        configuredOptions.with(SortingOrder.GIVEN);

        OmittingOptions omittingOptions = new OmittingOptions();
        configuredOptions.with(Omitting.BYFIELDNAMES_OPTIONNAME, omittingOptions.byFieldNames());
        configuredOptions.with(Omitting.BYFIELDANNOTATIONS_OPTIONNAME, omittingOptions.byFieldAnnotations());
        configuredOptions.with(Omitting.BYFIELDCLASSES_OPTIONNAME, omittingOptions.byFieldClasses());
        Component configured = new Component(configuredOptions);

        Assert.assertTrue(configured.isValid());
        configured.initializeSerializingInstance();

        OUT_Ser_1 out_Ser_1 = new OUT_Ser_1();

        Assert.assertEquals(XMLSERIALIZED_OUT_EXPECTED_GIVEN, configured.serialize(out_Ser_1));
    }

    @Test
    public void testSerialize_Defaults() {

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        SortingOptions sortingOptions = new SortingOptions();
        configuredOptions.with(sortingOptions.direction());
        configuredOptions.with(sortingOptions.order());

        OmittingOptions omittingOptions = new OmittingOptions();
        configuredOptions.with(Omitting.BYFIELDNAMES_OPTIONNAME, omittingOptions.byFieldNames());
        configuredOptions.with(Omitting.BYFIELDANNOTATIONS_OPTIONNAME, omittingOptions.byFieldAnnotations());
        configuredOptions.with(Omitting.BYFIELDCLASSES_OPTIONNAME, omittingOptions.byFieldClasses());
        Component configured = new Component(configuredOptions);

        Assert.assertTrue(configured.isValid());
        configured.initializeSerializingInstance();

        OUT_Ser_1 out_Ser_1 = new OUT_Ser_1();

        Assert.assertEquals(XMLSERIALIZED_OUT_EXPECTED_DEFAULT, configured.serialize(out_Ser_1));
    }

}
