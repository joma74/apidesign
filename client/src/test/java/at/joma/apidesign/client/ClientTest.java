package at.joma.apidesign.client;

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
import at.joma.apidesign.component.l2.provider.api.AsXML;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;

@RunWith(CdiRunner.class)
@AdditionalClasses({ ComponentProducer.class })
public class ClientTest {

	private static final Logger LOG = LoggerFactory.getLogger(ClientTest.class);

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

	@Test
	public void testComponent() {
		Assert.assertNotNull(asXmlDefault);

		asXmlDefault.serialize(null);
		
		LOG.info(asXmlDefault.toString());
	}

}
