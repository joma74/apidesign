package at.joma.apidesign.component.l2.provider.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer.Configured;

@RunWith(CdiRunner.class)
public class ComponentCacheHolderTest {

	@Inject
	@Named(ComponentCacheHolder.CDI_NAME)
	private ComponentCacheHolder il2componentCacheHolder;

	@Test
	public void checkCacheStats() {
		il2componentCacheHolder.logCacheStats();
	}

	@Test
	public void checkCanCache() {

		ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
		configuredOptions.with(SortingDirection.ASC);
		configuredOptions.with(SortingOrder.ALPHABETICALLY);
		configuredOptions.with(Configured.GLOBALFIELDS_OPTIONNAME, new String[] { "_parent" });
		Configured configured = new Configured(configuredOptions);
		il2componentCacheHolder.put(configuredOptions, configured);
		Assert.assertThat(il2componentCacheHolder.getCache().size(), is(1l));

		il2componentCacheHolder.logCacheStats("On Baseline");

		Assert.assertNotNull(il2componentCacheHolder.getIfPresent(configuredOptions));
		il2componentCacheHolder.logCacheStats("On 1st get");

		ConfiguredOptionsHolder configuredOptionsClone_1 = (ConfiguredOptionsHolder) configuredOptions.deepClone();
		il2componentCacheHolder.put(configuredOptionsClone_1, configured);
		Assert.assertThat(il2componentCacheHolder.getCache().size(), is(1l));

		Assert.assertNotNull(il2componentCacheHolder.getIfPresent(configuredOptionsClone_1));
		il2componentCacheHolder.logCacheStats("On 2nd get");

		// force garbage collection
		System.gc();
		il2componentCacheHolder.logCacheStats("On 1st System.gc()");
		Assert.assertNotNull(il2componentCacheHolder.getIfPresent(configuredOptionsClone_1));
		il2componentCacheHolder.logCacheStats("On 3rd get");

		configured = null;
		il2componentCacheHolder.logCacheStats("On nulling weak referenced cached value 'configured' before 2nd System.gc()");
		// force garbage collection
		System.gc();
		assertNull(il2componentCacheHolder.getIfPresent(configuredOptions));
		il2componentCacheHolder.logCacheStats("On 4th get after 2nd System.gc(). See CacheBuilder#weakValues to explain 'missCount=1 but ?eventual size=1'"); // hitCount=2,
		assertNull(il2componentCacheHolder.getIfPresent(configuredOptionsClone_1));
		il2componentCacheHolder.logCacheStats("On 5th get after 2nd System.gc(). See CacheBuilder#weakValues to explain 'missCount=2 but ?eventual size=1'"); // hitCount=2,
																																				// missCount=1,
	}

}
