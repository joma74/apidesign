package at.itsv.ta2mig.runtime.serializer.beancopier;

/**
 * Signals a failure of the JavaBeanCopier.
 * 
 * @author joerg
 */
public class JavaBeanCopierFailure extends RuntimeException {

    private static final long serialVersionUID = 8508920411259689314L;

    public JavaBeanCopierFailure() {
        super();
    }

    public JavaBeanCopierFailure(String message) {
        super(message);
    }

    public JavaBeanCopierFailure(String message, Throwable cause) {
        super(message, cause);
    }

    public JavaBeanCopierFailure(Throwable cause) {
        super(cause);
    }

}
