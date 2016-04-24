package at.joma.stackoverflow.cdi;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

public class Producer {

	public static final String VALUE_B = "valueB";

	public static final String VALUE_A = "valueA";

	@Inject
	SomeCache someCache;

	@Produces
	public Product produceProduct(InjectionPoint ip) {
		if (!VALUE_B.equals(someCache.getCacheValue())) {
			someCache.setCacheValue(VALUE_A);
		} else {
			someCache.setCacheValue(VALUE_B);
		}
		return new Product(someCache.getCacheValue());
	}

}
