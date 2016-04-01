package at.joma.apidesign.component.l2.client.api;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.f.Omitting;
import at.joma.apidesign.component.l2.client.api.f.Sorting;

public interface IL2Component extends IL1Component {

    @SuppressWarnings("all")
    public abstract class SortingAnnotation extends AnnotationLiteral<Sorting> implements Sorting {

        private static final long serialVersionUID = 1304991043750706771L;

    }

    @SuppressWarnings("all")
    public abstract class OmittingAnnotation extends AnnotationLiteral<Omitting> implements Omitting {

        private static final long serialVersionUID = 7718657788002609185L;

    }
    
}
