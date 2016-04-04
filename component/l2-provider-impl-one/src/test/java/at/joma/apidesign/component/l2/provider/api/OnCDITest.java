package at.joma.apidesign.component.l2.provider.api;

import java.util.Arrays;

import javax.inject.Inject;

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
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer.Configured;

@RunWith(CdiRunner.class)
@AdditionalClasses({ ComponentProducer.class })
public class OnCDITest {

	private static final Logger LOG = LoggerFactory.getLogger(OnCDITest.class);

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
	IL1Component asXmlDefaultSame;

	@Inject
	@AsXML
	@Sorting(order = SortingOrder.GIVEN, direction = SortingDirection.NONE)
	@Omitting(globalFields = { "_parent" })
	IL1Component asXmlGiven;

	@Test
	public void testDefaultSettings() {

		ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
		configuredOptions//
				.with(SortingOrder.ALPHABETICALLY)//
				.with(SortingDirection.ASC)//
				.with(Configured.GLOBALFIELDS_OPTIONNAME, new String[] {})//
		;

		Assert.assertNotNull(asXmlDefault);

		LOG.info(asXmlDefault.printConfiguration());

		Assert.assertTrue(ListUtils.isEqualList(Arrays.asList(configuredOptions.getConfiguration()), Arrays.asList(asXmlDefault.getConfiguration())));
	}

	@Test
	public void testGivenSettings() {

		ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
		configuredOptions//
				.with(SortingOrder.GIVEN)//
				.with(SortingDirection.NONE)//
				.with(Configured.GLOBALFIELDS_OPTIONNAME, new String[] { "_parent" })//
		;

		Assert.assertNotNull(asXmlGiven);

		LOG.info(asXmlGiven.printConfiguration());

		Assert.assertTrue(ListUtils.isEqualList(Arrays.asList(configuredOptions.getConfiguration()), Arrays.asList(asXmlGiven.getConfiguration())));
	}

	@Test
	public void testForSameness() {
		
		Assert.assertSame(asXmlDefault, asXmlDefaultSame);
	}

}
