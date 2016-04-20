package at.joma.apidesign.component.l2.provider.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.enterprise.context.spi.CreationalContext;
import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.provider.api.multithreadutils.InjectionPerThreadScope;
import at.joma.apidesign.component.l2.provider.api.multithreadutils.InjectionSameScope;
import at.joma.apidesign.component.l2.provider.api.multithreadutils.ManagedInstanceCreator;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;

@RunWith(CdiRunner.class)
@AdditionalClasses({
    ComponentProducer.class
})
public class CDIMultiThreadTest {

    private static final Logger LOG = LoggerFactory.getLogger(CDIMultiThreadTest.class);

    @Rule
    public TestRule getWatchman() {
        return new TestWatcher() {

            @Override
            protected void starting(Description description) {
                LOG.info("*********** Running {} ***********", description.getMethodName());
            }
        };
    }

    @Inject
    ManagedInstanceCreator instanceCreator;

    @Inject
    @AsXML
    IL1Component asXmlWarmup;

    private static final int THREAD_BATCH_SIZE = 10;

    private static final int ROUNDS = 5;

    @Test
    public void testMultiThreaded_PerThreadScopeInjections() throws Exception {

        long start = System.nanoTime();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_BATCH_SIZE);

        for (int i = 0; i < ROUNDS; i++) {
            final List<Callable<Void>> callables = createNewBatchOfThreadScopeInjections();
            List<Future<Void>> results = executor.invokeAll(callables);
            rethrowOnException(results);
        }
        executor.shutdown();

        long duration = (System.nanoTime() - start);
        double milliseconds = duration / 1e6;
        LOG.info("Created " + THREAD_BATCH_SIZE * ROUNDS + " multithreaded per thread scope injections took " + milliseconds + "[MS], t.i. " + milliseconds
                / (THREAD_BATCH_SIZE * ROUNDS) + "[MS]/instance");

    }

    private void rethrowOnException(List<Future<Void>> results) throws Exception {
        for (Future<Void> result : results) {
            try {
                result.get();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw e;
            }
        }
    }

    private List<Callable<Void>> createNewBatchOfThreadScopeInjections() {
        final List<Callable<Void>> callables = new ArrayList<>();
        for (int i = 0; i < THREAD_BATCH_SIZE; i++) {
            createPerThreadScopeInjections(callables);
        }
        return callables;
    }

    private void createPerThreadScopeInjections(final List<Callable<Void>> callables) {
        final InjectionPerThreadScope aCallable = new InjectionPerThreadScope();
        Object[] result = instanceCreator.activateCDI(aCallable);
        aCallable.setCreationalContext((CreationalContext) result[1]);
        callables.add(aCallable);
    }

    @Test
    public void testMultiThreaded_SameScopeInjections() throws Exception {

        long start = System.nanoTime();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_BATCH_SIZE);

        for (int i = 0; i < ROUNDS; i++) {
            final List<Callable<Void>> callables = createNewBatchOfSameScopeInjections();
            List<Future<Void>> results = executor.invokeAll(callables);
            rethrowOnException(results);
        }
        executor.shutdown();

        long duration = (System.nanoTime() - start);
        double milliseconds = duration / 1e6;
        LOG.info("Created " + THREAD_BATCH_SIZE * ROUNDS + " multithreaded same scope injections took " + milliseconds + "[MS], t.i. " + milliseconds
                / (THREAD_BATCH_SIZE * ROUNDS) + "[MS]/instance");
    }

    private List<Callable<Void>> createNewBatchOfSameScopeInjections() {
        final List<Callable<Void>> callables = new ArrayList<>();
        for (int i = 0; i < THREAD_BATCH_SIZE; i++) {
            createSameScopeInjections(callables);
        }
        return callables;
    }

    private void createSameScopeInjections(final List<Callable<Void>> callables) {
        final InjectionSameScope aCallable = new InjectionSameScope();
        Object[] result = instanceCreator.activateCDI(aCallable);
        aCallable.setCreationalContext((CreationalContext) result[1]);
        callables.add(aCallable);
    }

}
