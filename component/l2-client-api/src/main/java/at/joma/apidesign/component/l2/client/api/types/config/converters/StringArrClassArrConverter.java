package at.joma.apidesign.component.l2.client.api.types.config.converters;

import java.util.ArrayList;
import java.util.List;


public class StringArrClassArrConverter extends TypeConverter<String[], Class<?>[]> {

    private static final long serialVersionUID = -1303220619470042119L;

    StringClassConverter stringClassConverter = new StringClassConverter();

    @Override
    public Class<?>[] convert(String[] classNames) {
        List<Class<?>> result = new ArrayList<>();
        for (String className : classNames) {
            result.add(stringClassConverter.convert(className));
        }
        return result.toArray(new Class<?>[result.size()]);
    }

}
