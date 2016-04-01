package at.joma.apidesign.component.l2.client.api.f;

import javax.enterprise.inject.spi.CDI;

import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.IL2Component.OmittingQualifier;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;

public class Builder implements ISortingBuilder, IOmittingBuilder {

    private SortingOrder sortingOrder = SortingOrder.ALPHABETICALLY;

    private SortingDirection sortingDirection = SortingDirection.ASC;

    private String[] globalFields = new String[]{};

    @Override
    public Builder with(SortingDirection direction) {
        return null;
    }

    @Override
    public Builder with(SortingOrder order) {
        return null;
    }

    @Override
    public Builder with(String[] globalFields) {
        return null;
    }

    public IL2Component build() {
        
        SortingQualifier sortingQualifier = new SortingQualifier() {

            private static final long serialVersionUID = 3395353165326173089L;

            @Override
            public SortingOrder order() {
                return Builder.this.sortingOrder;
            }

            @Override
            public SortingDirection direction() {
                return Builder.this.sortingDirection;
            }
        };
        
        OmittingQualifier omittingQualifier = new OmittingQualifier() {

            private static final long serialVersionUID = -1139593265279393709L;

            @Override
            public String[] globalFields() {
                return Builder.this.globalFields;
            }
        };

        IL2Component returnValue = CDI.current().select(IL2Component.class, sortingQualifier, omittingQualifier).get(); //NOSONAR justification: returnValue for debugging

        return returnValue;
    }
}
