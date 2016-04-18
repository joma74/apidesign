package at.joma.apidesign.component.l2.provider.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import at.joma.apidesign.cdiutilsmultithreaded.ManagedInstanceCreator;
import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.provider.api.multithreadutils.InjectionPerThreadScope;
import at.joma.apidesign.component.l2.provider.api.multithreadutils.InjectionSameScope;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;

@RunWith(CdiRunner.class)
@AdditionalClasses({ ComponentProducer.class })
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

	private static final int ROUNDS = 100000;

	@Test
	public void testMultiThreaded_PerThreadScopeInjections() throws Exception {

		long start = System.currentTimeMillis();

		final List<Callable<Void>> callables = new ArrayList<>();
		for (int i = 0; i < ROUNDS; i++) {
			createPerThreadScopeInjections(callables);
		}
		ExecutorService executor = Executors.newFixedThreadPool(50);
		executor.invokeAll(callables);
		executor.shutdown();

		long duration = (System.nanoTime() - start);
		double milliseconds = duration / 1e6;
		LOG.info("Creating " + ROUNDS + " multithreaded per thread scope injections took " + milliseconds + "[MS], t.i. " + milliseconds / ROUNDS + "[MS]/instance");

	}

	private void createPerThreadScopeInjections(final List<Callable<Void>> callables) {
		final InjectionPerThreadScope aCallable = new InjectionPerThreadScope();
		instanceCreator.activateCDI(aCallable);
		callables.add(aCallable);
	}

	@Test
	public void testMultiThreaded_SameScopeInjections() throws Exception {

		long start = System.nanoTime();

		final List<Callable<Void>> callables = new ArrayList<>();
		for (int i = 0; i < ROUNDS; i++) {
			createSameScopeInjections(callables);
		}
		ExecutorService executor = Executors.newFixedThreadPool(50);
		executor.invokeAll(callables);
		executor.shutdown();

		long duration = (System.nanoTime() - start);
		double milliseconds = duration / 1e6;
		LOG.info("Creating " + ROUNDS + " multithreaded same scope injections injections took " + milliseconds + "[MS], t.i. " + milliseconds / ROUNDS + "[MS]/instance");
	}

	private void createSameScopeInjections(final List<Callable<Void>> callables) {
		final InjectionSameScope aCallable = new InjectionSameScope();
		instanceCreator.activateCDI(aCallable);
		callables.add(aCallable);
	}

}
