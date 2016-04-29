package at.joma.apidesign.component.l2.client.api.types.config.converters;

import java.io.Serializable;


public abstract class TypeConverter<S, T> implements Serializable{

    private static final long serialVersionUID = -4929101995528928873L;

    public abstract T convert(S source);

}
