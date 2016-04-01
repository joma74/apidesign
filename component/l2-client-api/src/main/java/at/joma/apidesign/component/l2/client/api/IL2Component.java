package at.joma.apidesign.component.l2.client.api;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.f.Omitting;
import at.joma.apidesign.component.l2.client.api.f.Sorting;

public interface IL2Component extends IL1Component {

    public static final AnnotationLiteral<Sorting> Sorting_ANNLIT = new AnnotationLiteral<Sorting>() {

        private static final long serialVersionUID = 7329210962797816290L;
    };

    @SuppressWarnings("all")
    public abstract class SortingQualifier extends AnnotationLiteral<Sorting> implements Sorting {

        private static final long serialVersionUID = 1304991043750706771L;

    }

    public static final AnnotationLiteral<Omitting> Omitting_ANNLIT = new AnnotationLiteral<Omitting>() {

        private static final long serialVersionUID = -6779195266944153494L;
    };

    @SuppressWarnings("all")
    public abstract class OmittingQualifier extends AnnotationLiteral<Sorting> implements Omitting {

        private static final long serialVersionUID = 7718657788002609185L;

    }

}
