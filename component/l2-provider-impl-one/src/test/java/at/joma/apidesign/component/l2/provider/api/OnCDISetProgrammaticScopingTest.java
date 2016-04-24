package at.joma.apidesign.component.l2.provider.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.provider.api.multithreadutils.ManagedInstanceCreator;
import at.joma.apidesign.component.l2.provider.impl.ComponentCacheHolder;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;

@RunWith(CdiRunner.class)
@AdditionalClasses({ ComponentProducer.class })
public class OnCDISetProgrammaticScopingTest {

	private static final Logger LOG = LoggerFactory.getLogger(OnCDISetProgrammaticScopingTest.class);

	@Inject
	ManagedInstanceCreator instanceCreator;

	@Inject
	BeanManager beanManager;

	@Inject
	ContextController contextController;

	@Test
	public void testDefaultScopenessOfManagedBean() {
		logScopeOfBeanType(OnCDISetProgrammaticScopingTest.class);
		logScopeOfBeanType(ComponentCacheHolder.class);
		logIsBeaninScope(ApplicationScoped.class, ComponentCacheHolder.class);
		logIsBeaninScope(RequestScoped.class, ComponentCacheHolder.class);
		//
		ComponentHolderWithDefaultScope chwrs_R1_1 = (ComponentHolderWithDefaultScope) instanceCreator.activateCDI(new ComponentHolderWithDefaultScope())[0];
		logIsBeaninScope(ApplicationScoped.class, ComponentCacheHolder.class);
		//
		ComponentHolderWithDefaultScope chwrs_R1_2 = (ComponentHolderWithDefaultScope) instanceCreator.activateCDI(new ComponentHolderWithDefaultScope())[0];
		logIsBeaninScope(ApplicationScoped.class, ComponentCacheHolder.class);
		// Assertion
		Assert.assertEquals(chwrs_R1_1.component.hashCode(), chwrs_R1_2.component.hashCode());
	}

	@Test
	public void testRequestScopenessOfManagedBean() {
		logScopeOfBeanType(OnCDISetProgrammaticScopingTest.class);
		logScopeOfBeanType(ComponentCacheHolder.class);
		logIsBeaninScope(ApplicationScoped.class, ComponentCacheHolder.class);
		logIsBeaninScope(RequestScoped.class, ComponentCacheHolder.class);
		//
		LOG.info(System.lineSeparator() + "------- About to open the first request scope -------");
		//
		contextController.openRequest();
		ComponentHolderWithRequestScope chwrs_R1_1 = (ComponentHolderWithRequestScope) instanceCreator.activateCDI(new ComponentHolderWithRequestScope())[0];
		ComponentHolderWithRequestScope chwrs_R1_2 = (ComponentHolderWithRequestScope) instanceCreator.activateCDI(new ComponentHolderWithRequestScope())[0];

		logIsBeaninScope(ApplicationScoped.class, ComponentCacheHolder.class);
		logIsBeaninScope(RequestScoped.class, ComponentCacheHolder.class);

		// Assertion
		Assert.assertEquals(chwrs_R1_1.component.hashCode(), chwrs_R1_2.component.hashCode());
		contextController.closeRequest();
		//
		LOG.info(System.lineSeparator() + "------- About to open the second request scope -------");
		//
		contextController.openRequest();
		ComponentHolderWithRequestScope chwrs_R2_1 = (ComponentHolderWithRequestScope) instanceCreator.activateCDI(new ComponentHolderWithRequestScope())[0];
		ComponentHolderWithRequestScope chwrs_R2_2 = (ComponentHolderWithRequestScope) instanceCreator.activateCDI(new ComponentHolderWithRequestScope())[0];

		logIsBeaninScope(ApplicationScoped.class, ComponentCacheHolder.class);
		logIsBeaninScope(RequestScoped.class, ComponentCacheHolder.class);

		// Assertion
		Assert.assertEquals(chwrs_R2_1.component.hashCode(), chwrs_R2_2.component.hashCode());
		contextController.closeRequest();

	}
	
	@Test
	public void testApplicationScopenessOfManagedBean() {
		logScopeOfBeanType(OnCDISetProgrammaticScopingTest.class);
		logScopeOfBeanType(ComponentCacheHolder.class);
		logIsBeaninScope(ApplicationScoped.class, ComponentCacheHolder.class);
		logIsBeaninScope(RequestScoped.class, ComponentCacheHolder.class);
		//
		LOG.info(System.lineSeparator() + "------- About to open the first request scope -------");
		//
		contextController.openRequest();
		ComponentHolderWithApplicationScope chwrs_R1_1 = (ComponentHolderWithApplicationScope) instanceCreator.activateCDI(new ComponentHolderWithApplicationScope())[0];
		ComponentHolderWithApplicationScope chwrs_R1_2 = (ComponentHolderWithApplicationScope) instanceCreator.activateCDI(new ComponentHolderWithApplicationScope())[0];

		logIsBeaninScope(ApplicationScoped.class, ComponentCacheHolder.class);
		logIsBeaninScope(RequestScoped.class, ComponentCacheHolder.class);

		// Assertion
		Assert.assertEquals(chwrs_R1_1.component.hashCode(), chwrs_R1_2.component.hashCode());
		contextController.closeRequest();
		//
		LOG.info(System.lineSeparator() + "------- About to open the second request scope -------");
		//
		contextController.openRequest();
		ComponentHolderWithApplicationScope chwrs_R2_1 = (ComponentHolderWithApplicationScope) instanceCreator.activateCDI(new ComponentHolderWithApplicationScope())[0];
		ComponentHolderWithApplicationScope chwrs_R2_2 = (ComponentHolderWithApplicationScope) instanceCreator.activateCDI(new ComponentHolderWithApplicationScope())[0];

		logIsBeaninScope(ApplicationScoped.class, ComponentCacheHolder.class);
		logIsBeaninScope(RequestScoped.class, ComponentCacheHolder.class);

		// Assertion
		Assert.assertEquals(chwrs_R2_1.component.hashCode(), chwrs_R2_2.component.hashCode());
		contextController.closeRequest();

	}

	public class ComponentHolderWithDefaultScope {

		@Inject
		@AsXML
		IL1Component component;
	}

	public class ComponentHolderWithRequestScope {

		@Inject
		@AsXML(inScope = RequestScoped.class)
		IL1Component component;
	}
	
	public class ComponentHolderWithApplicationScope {

		@Inject
		@AsXML(inScope = ApplicationScoped.class)
		IL1Component component;
	}

	private void logScopeOfBeanType(Class<?> beanType) {
		LOG.info(System.lineSeparator() + "The class " + beanType.getName() + " is running in scope " + instanceCreator.getScopeOfBean(beanType));
	}

	private void logIsBeaninScope(Class<? extends Annotation> scope, Type beanType, Annotation... qualifiers) {
		Object o = instanceCreator.getBeanInScope(scope, beanType, qualifiers);
		if (o != null) {
			LOG.info(System.lineSeparator() + "In scope >>" + scope.getSimpleName() + "<< the instance >>" + o + "<< is found.");
		} else {
			LOG.info(System.lineSeparator() + "In scope >>" + scope.getSimpleName() + "<< no instance of >>" + beanType.toString() + "<< is found.");
		}
	}
}
