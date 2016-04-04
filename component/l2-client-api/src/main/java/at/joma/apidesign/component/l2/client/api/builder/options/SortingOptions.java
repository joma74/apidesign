package at.joma.apidesign.component.l2.client.api.builder.options;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;

@Sorting
public class SortingOptions extends AnnotationLiteral<Sorting> implements Sorting {

    private static final long serialVersionUID = 4304876169606777565L;

    private SortingDirection sortingDirection = this.getClass().getAnnotation(Sorting.class).direction();

    private SortingOrder sortingOrder = this.getClass().getAnnotation(Sorting.class).order();

    @Override
    public SortingDirection direction() {
        return this.sortingDirection;
    }

    @Override
    public SortingOrder order() {
        return this.sortingOrder;
    }

    public void setSortingDirection(SortingDirection sortingDirection) {
        this.sortingDirection = sortingDirection;
    }

    public void setSortingOrder(SortingOrder sortingOrder) {
        this.sortingOrder = sortingOrder;
    }
}
