package at.joma.apidesign.component.l2.provider.impl.xstream;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.FieldKey;
import com.thoughtworks.xstream.converters.reflection.FieldKeySorter;
import com.thoughtworks.xstream.core.util.OrderRetainingMap;

/**
 * Extension of {@link XStream} that solves the implementation part of
 * {@link AsXmlWithAlphabeticSortedFields}.
 * 
 * @author joerg
 * @see XStreamProducer#produceWithAlphabeticSortedFields()
 */
@SuppressWarnings("deprecation")
public class AlphabetKeySorter implements FieldKeySorter {

    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    @Override
    public Map sort(Class type, Map keyedByFieldKey) {
        Map result = new OrderRetainingMap();
        List<FieldKey> keys = new LinkedList<>(keyedByFieldKey.keySet());
        Collections.sort(keys, new Comparator<FieldKey>() {

            @Override
            public int compare(FieldKey first, FieldKey second) {
                return first.getFieldName().compareTo(second.getFieldName());
            }
        });
        for (FieldKey next : keys) {
            result.put(next, keyedByFieldKey.get(next));
        }
        return result;
    }

}
