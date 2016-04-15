package at.joma.apidesign.component.l2.provider.impl.xstream;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * {@link XStream} extension that does
 * <em>NOT<em> serialize a field(synonym member) instance where any of the following applies  
 * <ul>
 * <li>Has an @Inject annotation<\li>
 * </ul>
 */
class OmittingMapperWrapper extends MapperWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(OmittingMapperWrapper.class);

    public OmittingMapperWrapper(final Mapper wrapped) {
        super(wrapped);
    }

    @Override
    public boolean shouldSerializeMember(@SuppressWarnings("rawtypes")
    final Class definedIn, final String fieldName) {
        final Field theField = getFieldFrom(definedIn, fieldName);
        if (theField != null && (isFieldWithInjectAnnotation(theField))) {
            return false;
        }
        return super.shouldSerializeMember(definedIn, fieldName);
    }

    protected Field getFieldFrom(@SuppressWarnings("rawtypes")
    final Class definedIn, final String fieldName) {
        try {
            return definedIn.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException e) {
            LOG.trace("ignored exception", e);
        }
        return null;
    }

    protected boolean isFieldWithInjectAnnotation(final Field field) {
        return null != field.getAnnotation(Inject.class);
    }

    protected boolean isParent(final Field field) {
        return "parent".equals(field.getName());
    }
}
