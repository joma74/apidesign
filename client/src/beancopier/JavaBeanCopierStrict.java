package at.itsv.ta2mig.runtime.serializer.beancopier;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import at.itsv.ta2mig.runtime.serializer.DeserializeOptions;

public class JavaBeanCopierStrict extends AbstractJavaBeanCopier {

    /**
     * For the implementation specification see
     * {@link DeserializeOptions#STRICT_BEANSTYLE}
     * 
     * @see DeserializeOptions#STRICT_BEANSTYLE
     * @see AbstractJavaBeanCopier#adjustSetterMethodDescription(Object,
     *      PropertyDescriptor[])
     */
    @Override
    protected void adjustSetterMethodDescription(Object bean, PropertyDescriptor[] propertyDescriptors) throws IntrospectionException {
        // NOP
    }
}
