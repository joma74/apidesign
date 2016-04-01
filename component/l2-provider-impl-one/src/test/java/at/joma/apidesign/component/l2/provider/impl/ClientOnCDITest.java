package at.joma.apidesign.component.l2.provider.impl;

import javax.inject.Inject;

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
import at.joma.apidesign.component.l2.client.api.f.Omitting;
import at.joma.apidesign.component.l2.client.api.f.Sorting;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;
import at.joma.apidesign.component.l2.provider.api.AsXML;

@RunWith(CdiRunner.class)
@AdditionalClasses({
    ComponentProducer.class
})
public class ClientOnCDITest {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientOnCDITest.class);

    @Rule
    public TestRule getWatchman() {
        return new TestWatcher() {
            protected void starting(Description description) {
                LOG.info("*********** Running {} ***********", description.getMethodName());
            }
        };
    }


    @Inject
    @AsXML
    IL1Component asXmlDefault;

    @Inject
    @AsXML
    @Sorting(order = SortingOrder.GIVEN, direction = SortingDirection.NONE)
    @Omitting(globalFields = {
        "_parent"
    })
    IL1Component asXmlGiven;

    @Test
    public void testAsXmlDefault() {
        
        Assert.assertNotNull(asXmlDefault);

        asXmlDefault.serialize(null);

        LOG.info(asXmlDefault.toString());
    }

    @Test
    public void testAsXmlGiven() {
        
        Assert.assertNotNull(asXmlGiven);

        asXmlGiven.serialize(null);

        LOG.info(asXmlGiven.toString());
    }

}
