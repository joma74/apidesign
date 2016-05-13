package at.itsv.ta2mig.runtime.serializer;

import java.io.File;
import java.io.IOException;

/**
 * This is the main interface of the serializer API. It provides methods to
 * serialize and deserialize the fields of an object between it's java
 * representation and some string representation. Support for writing and
 * reading the string representation of a single object to and from a persistent
 * storage is file based and per file. That is one string representation of a
 * single object per file.
 * <p>
 * It's primary purpose stemmed from the fact that object representations are
 * sometimes hard to read and use during development, testing or logging for us
 * humans. Especially nested object structures or complex classes like ta2
 * groups or ta2 records and calendars, are hard to read and therefore hard to
 * share/alter/reuse/remember for us humans. Therefore the functionality of the
 * representer {@link SerializerFactory} was choosen to be added on those
 * code-generated classes it should represent. Especially on those types that
 * are used in interfaces between system APIs e.g. web services, file services,
 * db services.
 * <p>
 * The supported string representation as of now is {@link AsXml} and
 * {@link AsXmlWithAlphabeticSortedFields}. Future plans could support
 * additionally a string representation {@link AsJson}.
 * <p>
 * <em>DISTINCTION REMARK</em>
 * <p>
 * This API supports string representations of objects that are not
 * ta2-binary-format compatible. It is not intended as a supplement to or
 * replacement of the other ta2mig-runtime components. This module is an
 * extension in respect to the above mentioned features.
 */
public interface Serializer {

    /**
     * Deserialize the serialized single object representation as a new class
     * instance from some {@code file}. Filling style is done based on a
     * defaulting {@link DeserializeOptions} configuration.
     * 
     * @param file
     *            with the serialized single object representation.
     * @return the object representation in the {@code file}
     * @throws IOException
     *             if the file could not be handled successfully.
     */
    <T> T deserialize(final File file) throws IOException;

    /**
     * Deserialize the serialized single object representation as a new class
     * instance from some {@code file}. As an intermediate this one's content is
     * filled into the given {@code objectToFill}. This comes especially handy
     * when the objectToFill lives in a certain context e.g. a CDI bean. Filling
     * behaviour is done based on the defaulting {@link DeserializeOptions}
     * configuration.
     * 
     * @param file
     *            with the serialized single object representation.
     * @param objectToFill
     *            some existing object to fill from
     * @return the deserialized object
     * @throws IOException
     *             if the file could not be handled successfully.
     */
    <T> T deserialize(final File file, final T objectToFill) throws IOException;

    /**
     * Deserialize the serialized single object representation as a new class
     * instance from some file. As an intermediate this one's content is filled
     * into the given {@code objectToFill}. This comes especially handy when the
     * objectToFill lives in a certain context e.g. a CDI bean. Filling
     * behaviour is done based on the given {@code option} configuration.
     * 
     * @param file
     *            with the serialized single object representation.
     * @param objectToFill
     *            some existing object to fill from
     * @param option
     *            configuring the filling behaviour
     * @return the deserialized object
     * @throws IOException
     *             if the file could not be handled successfully.
     */
    <T> T deserialize(final File file, final T objectToFill, final DeserializeOptions option) throws IOException;

    /**
     * Deserialize the serialized single object representation as a new class
     * instance from some {@code string}.
     * 
     * @param string
     *            with the serialized single object representation.
     * @return the deserialized object
     */
    <T> T deserialize(final String string);

    /**
     * Deserialize the serialized single object representation as a new class
     * instance from some {@code string}. As an intermediate this one's content
     * is filled into the given {@code objectToFill}. This comes especially
     * handy when the objectToFill lives in a certain context e.g. a CDI bean.
     * Filling behaviour is done based on the defaulting
     * {@link DeserializeOptions} configuration.
     * 
     * @param string
     *            with the serialized single object representation.
     * @param objectToFill
     *            some existing object to fill from
     * @return the deserialized object
     */
    <T> T deserialize(final String string, final T objectToFill);

    /**
     * Deserialize the serialized single object representation as a new class
     * instance from some {@code string}. As an intermediate this one's content
     * is filled into the given {@code objectToFill}. This comes especially
     * handy when the objectToFill lives in a certain context e.g. a CDI bean.
     * Filling behaviour is done based on the given {code option} configuration.
     * 
     * @param string
     *            with the serialized single object representation.
     * @param objectToFill
     *            some existing object to fill from
     * @param option
     *            configuring the filling behaviour
     * @return the deserialized object
     */
    <T> T deserialize(final String string, final T objectToFill, final DeserializeOptions option);

    /**
     * Serializes a given {@code object}. The serializing behaviour is based on
     * a serializing configuration - see {@link SerializerFactory}.
     * 
     * @param object
     *            to serialize
     * @return the serialized object
     */
    String serialize(final Object object);

    /**
     * Serializes a given {@code object} into the given {@code file}. The
     * serializing behaviour is based on a serializing configuration - see
     * {@link SerializerFactory}.
     * 
     * @param object
     *            to serialize
     * @param file
     *            to fill the serialized single object representation into.
     * @return the filled file
     * @throws IOException
     *             if the file could not be handled successfully.
     */
    File serialize(final Object object, final File file) throws IOException;
}
