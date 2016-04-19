package at.joma.apidesign.component.l2.provider.api.multithreadutils;

import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.weld.environment.se.WeldSEBeanRegistrant;
import org.jboss.weld.environment.se.contexts.ThreadContext;
import org.jboss.weld.environment.se.contexts.ThreadScoped;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.provider.api.AsXML;

public class InjectionPerThreadScope implements Callable<Void> {

	private static final Logger LOG = LoggerFactory.getLogger(InjectionPerThreadScope.class);

	@Inject
	@ThreadScoped
	@AsXML
	@Sorting(order = SortingOrder.GIVEN, direction = SortingDirection.NONE)
	@Omitting(globalFields = { "_parent" })
	Instance<IL1Component> asXmlGiven;

	private ThreadContext threadContext;
	
	private CreationalContext creationalContext;

	@Inject
	@PostConstruct
	public void postConstruct(WeldSEBeanRegistrant extension) {
		this.threadContext = extension.getThreadContext();
	}
	
	public void setCreationalContext(CreationalContext creationalContext){
	    this.creationalContext = creationalContext;
	}

	@Override
	public Void call() throws Exception {
		try {
			threadContext.activate();
			IL1Component firstInstance = asXmlGiven.get();
			IL1Component secondInstance = asXmlGiven.get();
			Assert.assertEquals(firstInstance.hashCode(), secondInstance.hashCode()); // Caching works test
			if (LOG.isDebugEnabled()) {
				LOG.debug(System.lineSeparator() + "Instance(1) " + firstInstance.hashCode() + firstInstance.printConfiguration());
				LOG.debug(System.lineSeparator() + "Instance(2) " + secondInstance.hashCode() + secondInstance.printConfiguration());
			}
			return null;
		} finally {
			threadContext.invalidate();
			threadContext.deactivate();
//			creationalContext.release();
		}
	}

}
