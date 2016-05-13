package at.itsv.ta2mig.runtime.serializer.beancopier;

import at.itsv.ta2mig.runtime.serializer.DeserializeOptions;
import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.constraints.NotNull;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Common class of all JavaBeanCopier classes. For usage description see
 * {@link #copy(Object, Object)}.
 * 
 * @author joerg
 */
public abstract class AbstractJavaBeanCopier {

    private static final String IGNORED_PROPERTY_NAMED_CLASS = "class";

    /**
     * Copies properties from the given {@code from} via JavaBeans property
     * getters to the given {@code to} via their respective JavaBeans property
     * setters.
     * <p>
     * The copy procedure
     * <ul>
     * <li>happens non-recursive, so only JavaBeans property on the root level
     * of the {@code from} parameter and the {@code to} parameter are copied.</li>
     * <li>is done in a copy-by-value style in the applied setter in respect to
     * the value of the respective getter.</li>
     * </ul>
     * <p>
     * Additionally special copying procedure rules apply:
     * <ul>
     * <li>A null value in a property of the given {@code from} is ignored and
     * hence no setter is called in the given {@code to}.</li>
     * <li>A property in the given {@code from} named 'class' is always ignored.
     * </li>
     * <li>A property with an unaccessible read method of the given {@code from}
     * is ignored. Case is that via CDI created objects get 'special' at-runtime
     * constructed management fields. Those are describeable via property
     * descriptors but still happen to be unaccessible on their read methods -
     * That is returning null when asking for the described read-method.</li>
     * </ul>
     * 
     * @param fromObject
     *            source of the copy procedure
     * @param toObject
     *            target of the copy procedure
     * @see <a href=
     *      "http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html"
     *      >JavaBeans Spec</a>
     */
    public void copy(final Object fromObject, Object toObject) {
        try {
            final PropertyDescriptor[] pds = getSetterMethodDescriptions(toObject);
            copyUsingPropertyDescriptors(fromObject, toObject, pds);
        } catch (IntrospectionException e) {
            throw new JavaBeanCopierFailure(e);
        }
    }

    /**
     * Returns property descriptors of a bean describing the property setter
     * methods. Note that the abstract
     * {@link #adjustSetterMethodDescription(Object, PropertyDescriptor[])}
     * descriminates which write method per {@link PropertyDescriptor} should be
     * finally used.
     * <p>
     * IMPLEMENTATION REMARK
     * <p>
     * Note that {@link PropertyUtils} is used which happens to cache(presumably
     * via a 'static' keyword) all such already resolved property descriptors.
     * As this module offers the altering of the to-be-applied bean style via
     * {@link DeserializeOptions} on the same class for different given
     * {@code bean}s that cache MUST be reset before each {@link PropertyUtils}
     * usage.
     * 
     * @param bean
     * @return
     * @throws IntrospectionException
     */
    @NotNull
    protected PropertyDescriptor[] getSetterMethodDescriptions(final Object bean) throws IntrospectionException {
        PropertyUtils.clearDescriptors();
        final PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(bean);
        adjustSetterMethodDescription(bean, propertyDescriptors);
        return propertyDescriptors;
    }

    /**
     * Procedure where the copying via the given {@code pds} happens from the
     * given {@code from} to the given {@code to}.
     * 
     * @param fromObject
     *            source of the copy procedure
     * @param toObject
     *            target of the copy procedure
     * @param pds
     *            description of all JavaBean properties to apply for the copy
     *            procedure
     * @see #copy(Object, Object)
     */
    protected void copyUsingPropertyDescriptors(final Object fromObject, Object toObject, final PropertyDescriptor[] pds) {
        for (final PropertyDescriptor pd : pds) {
            final String propertyName = pd.getName();

            if (IGNORED_PROPERTY_NAMED_CLASS.equals(propertyName)) {
                continue;
            }

            final Method readMethod = getGetterMethod(fromObject, propertyName);

            // CDI beans often have readMethods which are not accessible, so ignore them
            if (readMethod != null) {
                final Object valueOfFrom = readValue(fromObject, propertyName, readMethod);

                // If the valueOfFrom is null, do NOT set that in the to
                if (valueOfFrom != null) {
                    final Method writeMethod = pd.getWriteMethod();
                    writeValue(toObject, propertyName, valueOfFrom, writeMethod);
                }
            }

        }
    }

    /**
     * Reads a value of a property named {@code propertyName} via the given
     * {@code readMethod} from the given {@code from} object.
     * 
     * @param from
     *            source of the to be read value
     * @param propertyName
     *            the name of the property to read (only used for reporting on
     *            failures)
     * @param readMethod
     *            the reflective method to use
     * @return the read value
     * @see #copy(Object, Object)
     */
    protected Object readValue(final Object from, final String propertyName, final Method readMethod) {
        Object valueOfFrom;
        try {
            if (readMethod != null) {
                valueOfFrom = readMethod.invoke(from);
            } else {
                throw new JavaBeanCopierFailure("Failure 'no read method' while trying to read value " + propertyName + " of " + System.lineSeparator() + from);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JavaBeanCopierFailure("Failure while trying to read value " + propertyName + " of " + System.lineSeparator() + from, e);
        }
        return valueOfFrom;
    }

    /**
     * Writes the given {@code valueOfFrom} value via the given
     * {@code writeMethod} to the given {@code to} object.
     * 
     * @param toObject
     *            target of the to be written value
     * @param propertyName
     *            the name of the property to write (only used for reporting on
     *            failures)
     * @param valueOfFrom
     *            the value to write
     * @param writeMethod
     *            the reflective method to use
     */
    protected void writeValue(Object toObject, String propertyName, Object valueOfFrom, Method writeMethod) {
        try {
            if (writeMethod != null) {
                writeMethod.invoke(toObject, valueOfFrom);
            } else {
                throw new JavaBeanCopierFailure("Failure 'no write method' while trying to write value " + valueOfFrom + " to " + propertyName + " of"
                        + System.lineSeparator() + toObject);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JavaBeanCopierFailure("Failure while trying to write value " + valueOfFrom + " to " + propertyName + "of" + toObject, e);
        }
    }

    /**
     * Returns the getter method for the given {@code bean} which is described
     * via the given {@code propertyName} by JavaBean standards. IMPLEMENTATION
     * REMARK
     * <p>
     * Note that this method returns null if
     * <ul>
     * <li>the given {@code bean} could not be described by the given
     * {@code propertyName}.</li>
     * <li>the description contains no refective read method.</li>
     * </ul>
     * 
     * @param bean
     *            to get the read method from
     * @param propertyName
     *            the name of the property to get the read method for
     * @return the so-found getter
     * @see #copy(Object, Object)
     */
    protected Method getGetterMethod(final Object bean, final String propertyName) {
        PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, propertyName);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new JavaBeanCopierFailure("Error while trying to get propertyDescriptor named " + propertyName + " from " + System.lineSeparator() + bean, e);
        }
        if (propertyDescriptor != null) {
            return propertyDescriptor.getReadMethod();
        }
        return null;
    }

    /**
     * Make some adjustment to the given setter descriptions
     * {@code propertyDescriptors} of the given {@code bean}.
     * 
     * @param bean
     *            which is described by the to be adjusted
     *            {@code propertyDescriptors}
     * @param propertyDescriptors
     *            describing the given {@code bean}
     * @throws IntrospectionException
     */
    protected abstract void adjustSetterMethodDescription(Object bean, PropertyDescriptor[] propertyDescriptors) throws IntrospectionException;

}
