package at.itsv.ta2mig.runtime.serializer.xstream;

import at.itsv.ta2mig.runtime.cobol.datatypes.TA2migCalendar;
import at.itsv.ta2mig.runtime.cobol.functions.behavior.ConversionHelper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Extension of {@link XStream} that solves the conversion of
 * {@link TA2migCalendar}.
 * 
 * @author joerg
 */
class XStreamTA2MigCalConverter extends AbstractSingleValueConverter {

    private final ConversionHelper conversionHelper;

    public XStreamTA2MigCalConverter(final ConversionHelper conversionHelper) {
        super();
        this.conversionHelper = conversionHelper;
    }

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") final Class type) {
        return type == TA2migCalendar.class;
    }

    @Override
    public Object fromString(final String str) {
        return this.conversionHelper.convertToCalendar(str);
    }

    @Override
    public String toString(final Object obj) {
        return this.conversionHelper.cobolToString(obj);
    }

}
