package at.joma.apidesign.l2.client.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import at.joma.apidesign.component.l1.client.api.types.ConfiguredOption;
import at.joma.apidesign.component.l2.client.api.IL2Component.Builder;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.provider.api.builder.AsXMLWithOptions;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer.Configured;

@RunWith(CdiRunner.class)
@AdditionalClasses({ ComponentProducer.class, Builder.class, })
public class ClientOnBuilderTest {

	private static final Logger LOG = LoggerFactory.getLogger(ClientOnBuilderTest.class);

	@Rule
	public TestRule getWatchman() {
		return new TestWatcher() {

			protected void starting(Description description) {
				LOG.info("*********** Running {} ***********", description.getMethodName());
			}
		};
	}

	@Test
	public void testComponent_With_GivenDescParent_OnBuilder() throws InstantiationException, IllegalAccessException {

		List<ConfiguredOption> configuredOptions = new ArrayList<ConfiguredOption>();

		ConfiguredOption sortingOrder_conf = new ConfiguredOption(SortingOrder.GIVEN);
		configuredOptions.add(sortingOrder_conf);

		ConfiguredOption sortingDirection_conf = new ConfiguredOption(SortingDirection.DESC);
		configuredOptions.add(sortingDirection_conf);

		ConfiguredOption globalFields_conf = new ConfiguredOption(Configured.GLOBALFIELDS_OPTIONNAME, new String[] { "_parent" });
		configuredOptions.add(globalFields_conf);

		Builder<AsXMLWithOptions> builder = new Builder<AsXMLWithOptions>(AsXMLWithOptions.class);

		IL1Component bean = builder//
				.with(SortingOrder.valueOf(sortingOrder_conf.value))//
				.with(SortingDirection.valueOf(sortingDirection_conf.value))//
				.with(globalFields_conf.convertValueToArray())//
				.build();

		Assert.assertNotNull(bean);

		LOG.info(bean.printConfiguration());

		Assert.assertTrue(ListUtils.isEqualList(configuredOptions, Arrays.asList(bean.getConfiguration())));
	}

	@Test
	public void testComponent_With_Default_OnBuilder() throws InstantiationException, IllegalAccessException {

		List<ConfiguredOption> configuredOptions = new ArrayList<ConfiguredOption>();

		ConfiguredOption sortingOrder_conf = new ConfiguredOption(SortingOrder.ALPHABETICALLY);
		configuredOptions.add(sortingOrder_conf);

		ConfiguredOption sortingDirection_conf = new ConfiguredOption(SortingDirection.ASC);
		configuredOptions.add(sortingDirection_conf);

		ConfiguredOption globalFields_conf = new ConfiguredOption(Configured.GLOBALFIELDS_OPTIONNAME, new String[] { "" });
		configuredOptions.add(globalFields_conf);

		Builder<AsXMLWithOptions> builder = new Builder<AsXMLWithOptions>(AsXMLWithOptions.class);

		IL1Component bean = builder//
				.build();

		Assert.assertNotNull(bean);

		LOG.info(bean.printConfiguration());

		Assert.assertTrue(ListUtils.isEqualList(configuredOptions, Arrays.asList(bean.getConfiguration())));
	}

}
