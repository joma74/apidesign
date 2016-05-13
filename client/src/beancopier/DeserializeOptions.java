package at.itsv.ta2mig.runtime.serializer;

/**
 * Options for configuring the deserializer behaviour. See {@link Serializer}
 * for it's API usage.
 */
public enum DeserializeOptions {

    /**
     * Filling of an object is done in a non-conventional aka relaxed JavaBeans
     * style using it's setter. Regarding the property's return type of it's
     * setter signature - which may be any type, not only void.
     * 
     * @see <a
     *      href="http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html"
     *      >JavaBeans Spec</a>
     */
    RELAXED_BEANSTYLE,

    /**
     * Filling of an object is done in a strict conventional JavaBeans style
     * using it's setter.
     * 
     * @see <a
     *      href="http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html">JavaBeans
     *      Spec</a>
     */
    STRICT_BEANSTYLE,
    
    /**
     * Filling of an object is done via reflective field setting.
     * 
     * @see <a
     *      href="http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html">JavaBeans
     *      Spec</a>
     */
    REFLECTIONSTYLE
    ;

}
