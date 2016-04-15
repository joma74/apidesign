package at.joma.apidesign.component.l2.provider.impl;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;

@Singleton
@Named(ComponentCacheHolder.CDI_NAME)
public class ComponentCacheHolder {

	public static final String CDI_NAME = "at.joma.apidesign.component.l2.provider.cache";

	private static final Logger LOG = LoggerFactory.getLogger(CDI_NAME);

	private Cache<ConfiguredOptionsHolder, Component> l2componentsCache;

	@SuppressWarnings("rawtypes")
	private final CacheBuilder cacheBuilder = CacheBuilder.newBuilder();

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		l2componentsCache = cacheBuilder.weakValues().maximumSize(50).recordStats().build();
	}

	public Cache<ConfiguredOptionsHolder, Component> getCache() {
		return l2componentsCache;
	}

	public CacheStats getCacheStats() {
		return l2componentsCache.stats();
	}

	public void logCacheStats() {
		if (LOG.isDebugEnabled()) {
			LOG.debug(printCacheStat());
		}
	}

	public Component getIfPresent(ConfiguredOptionsHolder configuredOptions) {
		return l2componentsCache.getIfPresent(configuredOptions);
	}

	public void put(ConfiguredOptionsHolder configuredOptions, Component iL2Component) {
		l2componentsCache.put(configuredOptions, iL2Component);
	}

	public void logCacheStats(String message) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(message + printCacheStat());
		}
	}

	private String printCacheStat() {
		return System.lineSeparator() + cacheBuilder.toString() + System.lineSeparator() + "CacheSize{size=" + l2componentsCache.size() + "}" + System.lineSeparator()
				+ getCacheStats().toString();
	}

}
