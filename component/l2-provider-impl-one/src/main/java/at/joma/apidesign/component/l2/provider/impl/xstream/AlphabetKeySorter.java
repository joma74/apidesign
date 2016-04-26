package at.joma.apidesign.component.l2.provider.impl.xstream;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.joma.apidesign.component.l2.client.api.types.SortingDirection;

import com.thoughtworks.xstream.converters.reflection.FieldKey;
import com.thoughtworks.xstream.converters.reflection.FieldKeySorter;
import com.thoughtworks.xstream.core.util.OrderRetainingMap;

@SuppressWarnings("deprecation")
public class AlphabetKeySorter implements FieldKeySorter {

    private final SortingDirection sortingDirection;

    public AlphabetKeySorter(SortingDirection sortingDirection) {
        this.sortingDirection = sortingDirection;
    }

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
                if (sortingDirection == SortingDirection.ASC) {
                    return first.getFieldName().compareTo(second.getFieldName());
                } else {
                    return second.getFieldName().compareTo(first.getFieldName());
                }

            }
        });
        for (FieldKey next : keys) {
            result.put(next, keyedByFieldKey.get(next));
        }
        return result;
    }

}
