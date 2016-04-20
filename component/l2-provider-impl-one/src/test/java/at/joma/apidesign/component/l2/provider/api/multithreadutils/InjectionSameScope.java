package at.joma.apidesign.component.l2.provider.api.multithreadutils;

import java.util.concurrent.Callable;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.provider.api.AsXML;

public class InjectionSameScope implements Callable<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(InjectionSameScope.class);

    @Inject
    @AsXML
    IL1Component first;

    @Inject
    @AsXML
    Instance<IL1Component> asXmlDefaultInstance;

    private CreationalContext creationalContext;

    public void setCreationalContext(CreationalContext creationalContext) {
        this.creationalContext = creationalContext;
    }

    @Override
    public Void call() throws Exception {
        IL1Component second = asXmlDefaultInstance.get();
        Assert.assertEquals(first.hashCode(), second.hashCode()); // Caching
                                                                  // works test
        if (LOG.isDebugEnabled()) {
            LOG.debug(System.lineSeparator() + "1st Instance asXmlDefault " + first.hashCode() + first.printConfiguration());
            LOG.debug(System.lineSeparator() + "2nd Instance asXmlDefault " + second.hashCode() + second.printConfiguration());
        }
        creationalContext.release();
        return null;
    }

}
