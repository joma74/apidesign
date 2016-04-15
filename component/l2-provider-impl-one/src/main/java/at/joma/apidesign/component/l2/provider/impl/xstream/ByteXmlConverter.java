package at.joma.apidesign.component.l2.provider.impl.xstream;

import org.apache.commons.lang3.StringEscapeUtils;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class ByteXmlConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return type == byte[].class;
    }

    @Override
    public Object fromString(String str) {
        return StringEscapeUtils.unescapeXml(str);
    }

    @Override
    public String toString(Object obj) {
        return StringEscapeUtils.escapeXml11(new String((byte[]) obj));
    }

}
