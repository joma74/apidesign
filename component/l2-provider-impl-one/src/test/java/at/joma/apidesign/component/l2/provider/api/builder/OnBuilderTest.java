package at.joma.apidesign.component.l2.provider.api.builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
import at.joma.apidesign.component.l2.provider.impl.Component;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;

@RunWith(CdiRunner.class)
@AdditionalClasses({ ComponentProducer.class, Builder.class, })
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
				.with(SortingDirection.NONE)//
				.with(Component.GLOBALFIELDS_OPTIONNAME, new String[] { "_parent" });

		Builder<AsXMLByBuilderOptions> builder = new Builder<>(AsXMLByBuilderOptions.class);

		IL1Component bean1 = builder//
				.with(configuredOptions.getValueFor(SortingOrder.class))//
				.with(configuredOptions.getValueFor(SortingDirection.class))//
				.with((String[]) configuredOptions.getValueFor(Component.GLOBALFIELDS_OPTIONNAME))//
				.build();

		IL1Component bean2 = builder//
				.with(configuredOptions.getValueFor(SortingOrder.class))//
				.with(configuredOptions.getValueFor(SortingDirection.class))//
				.with((String[]) configuredOptions.getValueFor(Component.GLOBALFIELDS_OPTIONNAME))//
				.build();

		Assert.assertSame(bean1, bean2);
	}

	@Test
	public void testComponent_With_Default() throws ReflectiveOperationException {

		ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
		configuredOptions//
				.with(SortingOrder.ALPHABETICALLY)//
				.with(SortingDirection.ASC)//
				.with(Component.GLOBALFIELDS_OPTIONNAME, new String[] { "" });

		Builder<AsXMLByBuilderOptions> builder = new Builder<>(AsXMLByBuilderOptions.class);

		IL1Component bean = builder//
				.build();

		Assert.assertNotNull(bean);

		LOG.info(bean.printConfiguration());
		
		Map<String, String> formatInfos_Expected = new HashMap<>();
		formatInfos_Expected.putAll(ComponentProducer.getFormatInfos());
		formatInfos_Expected.putAll(Component.getFormatInfos());
		
		Assert.assertEquals(formatInfos_Expected, bean.getFormatInfo());

		Assert.assertTrue(ListUtils.isEqualList(Arrays.asList(configuredOptions.getOptions()), Arrays.asList(bean.getOptions())));
	}

	@Test
	public void testComponent_With_GivenNoneParent() throws ReflectiveOperationException {

		ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder()//
				.with(SortingOrder.GIVEN)//
				.with(SortingDirection.NONE)//
				.with(Component.GLOBALFIELDS_OPTIONNAME, new String[] { "_parent" });
		
		IL1Component bean = new Builder<>(AsXMLByBuilderOptions.class)//
				.with(configuredOptions)//
				.build();

		Assert.assertNotNull(bean);

		LOG.info(bean.printConfiguration());

		Map<String, String> formatInfos_Expected = new HashMap<>();
        formatInfos_Expected.putAll(ComponentProducer.getFormatInfos());
        formatInfos_Expected.putAll(Component.getFormatInfos());
        
        Assert.assertEquals(formatInfos_Expected, bean.getFormatInfo());
		
		Assert.assertTrue(ListUtils.isEqualList(Arrays.asList(configuredOptions.getOptions()), Arrays.asList(bean.getOptions())));
	}
}
