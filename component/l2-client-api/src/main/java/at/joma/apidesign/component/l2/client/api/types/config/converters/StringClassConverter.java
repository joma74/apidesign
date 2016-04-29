package at.joma.apidesign.component.l2.client.api.types.config.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringClassConverter extends TypeConverter<String, Class<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(StringClassConverter.class);

    private static final long serialVersionUID = -2484581278717116604L;

    @Override
    public Class<?> convert(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
