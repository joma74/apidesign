package at.itsv.ta2mig.runtime.serializer.beancopier;

import at.itsv.ta2mig.runtime.serializer.DeserializeOptions;
import org.apache.log4j.Logger;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * JavaBeanCopier implementation of the
 * {@link DeserializeOptions#RELAXED_BEANSTYLE}.
 * 
 * @author joerg
 * @see AbstractJavaBeanCopier
 * @see DeserializeOptions#RELAXED_BEANSTYLE
 */
public class JavaBeanCopierRelaxed extends AbstractJavaBeanCopier {

    private static final Logger LOG = Logger.getLogger(JavaBeanCopierRelaxed.class);

    /**
     * For the implementation specification see
     * {@link DeserializeOptions#RELAXED_BEANSTYLE}
     * 
     * @see DeserializeOptions#RELAXED_BEANSTYLE
     * @see AbstractJavaBeanCopier#adjustSetterMethodDescription(Object, PropertyDescriptor[])
     */
    @Override
    protected void adjustSetterMethodDescription(final Object bean, final PropertyDescriptor[] propertyDescriptors) throws IntrospectionException {
        for (final PropertyDescriptor pd : propertyDescriptors) {
            if (pd.getWriteMethod() == null) {
                final String propertyName = pd.getName();
                final char upChar = Character.toUpperCase(propertyName.charAt(0));
                final String writeMethodName = "set" + upChar + propertyName.substring(1, propertyName.length());
                Method writeMethod;
                try {
                    writeMethod = bean.getClass().getDeclaredMethod(writeMethodName, pd.getReadMethod().getReturnType());
                } catch (NoSuchMethodException | SecurityException e) {
                    LOG.trace("ignored exception", e);
                    continue;
                }
                pd.setWriteMethod(writeMethod);
            }
        }
    }

}
