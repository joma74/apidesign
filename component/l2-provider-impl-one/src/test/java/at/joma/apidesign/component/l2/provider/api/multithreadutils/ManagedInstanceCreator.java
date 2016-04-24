package at.joma.apidesign.component.l2.provider.api.multithreadutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagedInstanceCreator {
	
	private static final Logger LOG = LoggerFactory.getLogger(ManagedInstanceCreator.class);

	@Inject
	BeanManager beanManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T getManagedInstance(final Class<T> beanType, final AnnotationLiteral annotationLiteral) {

		T result = null;

		final Set<Bean<?>> beanSet = beanManager.getBeans(beanType, annotationLiteral);
		for (final Bean<?> bean : beanSet) {
			final Set<Type> types = bean.getTypes();
			for (final Type type : types) {
				if (type.equals(beanType)) {
					final Bean<T> beanTyped = (Bean<T>) bean;
					result = beanTyped.create(beanManager.createCreationalContext(beanTyped));
				} else {
				}
			}
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Object[] activateCDI(T t) {

		final Class<?> aClass = t.getClass();
		final AnnotatedType annotationType = beanManager.createAnnotatedType(aClass);
		final InjectionTarget injectionTarget = beanManager.createInjectionTarget(annotationType);
		final CreationalContext creationalContext = beanManager.createCreationalContext(null);
		injectionTarget.inject(t, creationalContext);
		injectionTarget.postConstruct(t);
		return new Object[] { t, creationalContext };
	}

	@SuppressWarnings("unchecked")
	public <T extends Type> Bean<? extends Type> getBean(T beanType, Annotation... qualifiers) {
		return (Bean<? extends Type>) beanManager.resolve(beanManager.getBeans(beanType, qualifiers));
	}

	public <T extends Type> Class<? extends Annotation> getScopeOfBean(T beanType, Annotation... qualifiers) {
		return getBean(beanType, qualifiers).getScope();
	}

	public <T extends Type> Object getBeanInScope(Class<? extends Annotation> scope, T beanType, Annotation... qualifiers) {

		Bean<? extends Type> targetedBean = getBean(beanType, qualifiers);

		try {
	        if (beanManager.getContext(scope).isActive()) {
	    		Context beanContextOfConfig = beanManager.getContext(scope);
	    		return beanContextOfConfig.get(targetedBean);
	        } else {
	        	LOG.warn(System.lineSeparator() + "Scope >>" + scope.getSimpleName() + "<< is not active.");
	            return null;
	        }
	    } catch (final ContextNotActiveException e) {
	    	LOG.info(System.lineSeparator() + "Context >>" + scope.getSimpleName() + "<< is not active.");
	        return null;
	    }
	}

}
