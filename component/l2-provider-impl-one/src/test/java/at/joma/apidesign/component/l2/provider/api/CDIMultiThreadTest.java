package at.joma.apidesign.component.l2.provider.api;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.weld.environment.se.contexts.ThreadScoped;
import org.jboss.weld.environment.se.threading.RunnableDecorator;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.cdiutilsmultithreaded.ManagedInstanceCreator;
import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;

@RunWith(CdiRunner.class)
@AdditionalClasses({
    ComponentProducer.class,
    RunnableDecorator.class
})
public class CDIMultiThreadTest {

    Logger LOG = LoggerFactory.getLogger(CDIMultiThreadTest.class);

    @Inject
    ManagedInstanceCreator instanceCreator;

    private static final int MAX_ROUNDS = 100;

    /**
     * Just to initialized the CDI System first
     */
    @Inject
    @AsXML
    IL1Component asXmlWarmup;

    @Test
    @Ignore
    public void testMultiThreadedCDIInjection() throws Exception {

        long start = System.currentTimeMillis();

        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < MAX_ROUNDS; i++) {
            createInjectionVariantGiven(threads);
            createInjectionVariantDefault(threads);
        }
        for (final Thread thread : threads) {
            thread.start();
        }
        for (final Thread thread : threads) {
            thread.join();
        }

        LOG.info("Creating " + MAX_ROUNDS * 2 + " CDI components in multiple threads took " + (System.currentTimeMillis() - start) + "[MS]");

    }

    private void createInjectionVariantGiven(final List<Thread> threads) {
        final Thread thread_A = new Thread() {

            @Inject
            @ThreadScoped
            @AsXML
            @Sorting(order = SortingOrder.GIVEN, direction = SortingDirection.NONE)
            @Omitting(globalFields = {
                "_parent"
            })
            Instance<IL1Component> asXmlGiven;

            public void run() {
                IL1Component threadScopedInstance = asXmlGiven.get();
                LOG.debug(System.lineSeparator() + "Component Instance " + threadScopedInstance.hashCode() + threadScopedInstance.printConfiguration());
            }
        };
        instanceCreator.activateCDI(thread_A);
        threads.add(thread_A);
    }

    private void createInjectionVariantDefault(final List<Thread> threads) {
        final Thread thread_A = new Thread() {

            @Inject
            @AsXML
            IL1Component asXmlDefault;

            public void run() {
                LOG.debug(System.lineSeparator() + "Instance " + asXmlDefault.hashCode() + asXmlDefault.printConfiguration());
            }
        };
        instanceCreator.activateCDI(thread_A);
        threads.add(thread_A);
    }

}
