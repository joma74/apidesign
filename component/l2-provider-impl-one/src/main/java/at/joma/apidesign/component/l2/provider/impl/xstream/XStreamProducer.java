package at.joma.apidesign.component.l2.provider.impl.xstream;

import javax.enterprise.inject.Produces;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XStreamProducer {

    /**
     * Implementation of {@link AsXml}.
     * 
     * @return implementation of {@link AsXml}
     */
    @Produces
    public XStream doProduceWithoutAlphabetSorting() {

        com.thoughtworks.xstream.XStream configuredXStreamInstance = new com.thoughtworks.xstream.XStream() {

            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new OmittingMapperWrapper(next);
            }

        };

        configuredXStreamInstance.registerConverter(new ByteXmlConverter());

        return configuredXStreamInstance;
    }

    /**
     * Implementation of {@link AsXmlWithAlphabeticSortedFields}.
     * 
     * @return implementation of {@link AsXmlWithAlphabeticSortedFields}
     */
    @Produces
    public XStream produceWithAlphabeticSortedFields() {

        com.thoughtworks.xstream.XStream configuredXStreamInstance =
                new com.thoughtworks.xstream.XStream(new PureJavaReflectionProvider(new FieldDictionary(new AlphabetKeySorter()))) {

                    @Override
                    protected MapperWrapper wrapMapper(MapperWrapper next) {
                        return new OmittingMapperWrapper(next);
                    }
                };
        configuredXStreamInstance.registerConverter(new ByteXmlConverter());

        return configuredXStreamInstance;
    }

}
