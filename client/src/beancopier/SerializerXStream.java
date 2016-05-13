package at.itsv.ta2mig.runtime.serializer.xstream;

import at.itsv.ta2mig.com.google.common.base.Charsets;
import at.itsv.ta2mig.runtime.Ta2migRuntimeException;
import at.itsv.ta2mig.runtime.serializer.DeserializeOptions;
import at.itsv.ta2mig.runtime.serializer.Serializer;
import at.itsv.ta2mig.runtime.serializer.SerializerFactory;
import at.itsv.ta2mig.runtime.serializer.beancopier.AbstractJavaBeanCopier;
import at.itsv.ta2mig.runtime.serializer.beancopier.JavaBeanCopierRelaxed;
import at.itsv.ta2mig.runtime.serializer.beancopier.JavaBeanCopierStrict;
import at.itsv.ta2mig.runtime.serializer.error.SerializerIOException;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Implementation of the {@link Serializer} interface via {@link XStream} for
 * {@link SerializerFactory}. {@link XStream} works seamlessly on objects of all
 * classes. It works per default by accessing the fields of the given object via
 * reflection and using that's {@link Object#toString()} as value. Furthermore
 * {@link XStream} supports application-specific hooking into it's serialization
 * and deserialization procedure. {@link SerializerXStreamProducer} is the CDI Factory for
 * objects of this class in this module.
 * <p>
 * Additional features are:
 * <ul>
 * <li>{@link XStream} supports application-specific extensions into it's
 * serialization and deserialization procedure. For application of that extensions 
 * in this module see {@link XStreamProducer}.
 * <li>CDI managed beans contain CDI management-related fields which do not make
 * sense outside it's scope instance and which can not be resused outside it's
 * scope instance. Serialisation is therefore done upon unproxying the given
 * object beforehand if it is a CDI managed bean. Unproxying supports only the
 * WELD implementation of CDI.</li> </ul>
 * <p>
 * USAGE REMARK
 * <ul>
 * <li>Decoding of a file's bytes to a {@link String} is done via
 * {@link #DEFAULT_CHARSET}. Hence all files read by this
 * implementation MUST be encoded in {@link #DEFAULT_CHARSET}.</li>
 * <li>The default deserialization option is {@link #DEFAULT_DESERIALIZE_OPTION}.
 * Hence all deserialization operations without further override of this option
 * apply this as their default.</li>
 * </ul>
 * 
 * @author joerg
 * @see Serializer
 * @see SerializerFactory
 * @see XStream
 * @see SerializerXStreamProducer
 */
public class SerializerXStream implements Serializer {

    /**
     * The default charset to read bytes from a file to string.
     */
    private static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    /**
     * The default option for deserializing beans.
     */
    public static final DeserializeOptions DEFAULT_DESERIALIZE_OPTION = DeserializeOptions.STRICT_BEANSTYLE;

    /**
     * The {@link AbstractJavaBeanCopier} concretization for the
     * {@link DeserializeOptions#RELAXED_BEANSTYLE}.
     */
    private static final JavaBeanCopierRelaxed JAVA_BEAN_COPIER_RELAXED = new JavaBeanCopierRelaxed();

    /**
     * The {@link AbstractJavaBeanCopier} concretization for the for the
     * {@link DeserializeOptions#STRICT_BEANSTYLE}.
     */
    private static final JavaBeanCopierStrict JAVA_BEAN_COPIER_STRICT = new JavaBeanCopierStrict();

    /**
     * Logger to use.
     */
    private static final Logger LOG = Logger.getLogger(SerializerXStream.class);

    /**
     * WELD method for supporting unproxying.
     */
    private static final String UNPROXY_METHOD_NAME_WELD = "getTargetInstance";

    /**
     * The to-be-used xStream instance.
     */
    private XStream xStream;

    /**
     * Unproxying support only the WELD implementation of CDI.
     * <p>
     * IMPLEMENTATION REMARK
     * <p>
     * MUST never fail
     * 
     * @param object
     *            to unproxy
     * @return a potentially unproxied object
     */
    @SuppressWarnings("unchecked")
    public static <T> T unproxyIfWeld(final Object object) {
        Method theMethod = null;
        try {
            theMethod = object.getClass().getDeclaredMethod(UNPROXY_METHOD_NAME_WELD);
        } catch (NoSuchMethodException e) {
            LOG.trace("ignored exception", e);
        }

        try {
            return theMethod == null?(T) object:(T) theMethod.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new Ta2migRuntimeException("Error while trying to unproxy " + object, e);
        }
    }

    @Override
    public <T> T deserialize(final File file) throws IOException {
        final String asString = new String(Files.readAllBytes(Paths.get(file.toURI())), DEFAULT_CHARSET);
        return deserialize(asString);
    }

    /**
     * Uses {@link #DEFAULT_DESERIALIZE_OPTION} as default.
     */
    @Override
    public <T> T deserialize(final File file, final T objectToFill) throws SerializerIOException {
        return deserialize(file, objectToFill, DEFAULT_DESERIALIZE_OPTION);
    }

    @Override
    public <T> T deserialize(final File file, final T objectToFill, final DeserializeOptions option) throws SerializerIOException {
        final String asString;
        try {
            asString = new String(Files.readAllBytes(Paths.get(file.toURI())), DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new SerializerIOException("Cannot read file content: ", e);
        }
        return deserialize(asString, objectToFill, option);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(final String string) {
        return (T) this.xStream.fromXML(string);
    }

    /**
     * Uses {@link #DEFAULT_DESERIALIZE_OPTION} as default.
     */
    @Override
    public <T> T deserialize(final String string, final T objectToFill) {
        return deserialize(string, objectToFill, DEFAULT_DESERIALIZE_OPTION);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(final String string, final T objectToFill, final DeserializeOptions option) {

        if (option.equals(DeserializeOptions.RELAXED_BEANSTYLE)) {

            final T tempObject = (T) this.xStream.fromXML(string);
            JAVA_BEAN_COPIER_RELAXED.copy(tempObject, objectToFill);
        } else if (option.equals(DEFAULT_DESERIALIZE_OPTION)) {

            final T tempObject = (T) this.xStream.fromXML(string);
            JAVA_BEAN_COPIER_STRICT.copy(tempObject, objectToFill);
        } else {

            this.xStream.fromXML(string, objectToFill);
        }
        return objectToFill;
    }

    public XStream getXStream() {
        return this.xStream;
    }

    @Override
    public String serialize(final Object object) {
        return this.xStream.toXML(unproxyIfWeld(object));
    }

    @Override
    public File serialize(final Object object, final File file) throws IOException {
        if (file.isDirectory()) {
            throw new IOException("Can not write to file '" + file + "' because its is a directory.");
        }
        if (!file.createNewFile()) {
            LOG.warn ("Cannot create new file!");
        }

        if (!file.exists() || !file.canWrite()) {
            throw new IOException("Can not write to file '" + file + "' because it could not be created or ist not writeable.");
        }
        final String serializedObject = serialize(unproxyIfWeld(object));
        Files.write(Paths.get(file.toURI()), serializedObject.getBytes(DEFAULT_CHARSET));
        return file;
    }

    /**
     * Set a preconfigured {@link XStream} instance.
     * 
     * @param configuredXStream
     */
    public void setXStream(final XStream configuredXStream) {
        this.xStream = configuredXStream;
    }
}
