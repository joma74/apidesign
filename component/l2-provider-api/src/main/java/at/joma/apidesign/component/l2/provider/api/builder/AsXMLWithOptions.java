package at.joma.apidesign.component.l2.provider.api.builder;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.builder.IRequiredProviderOptions;

@AsXMLByBuilder
public class AsXMLWithOptions extends AnnotationLiteral<AsXMLByBuilder> implements AsXMLByBuilder, IRequiredProviderOptions {

    private static final long serialVersionUID = -3254824763865440793L;

    private Sorting sorting = this.getClass().getAnnotation(AsXMLByBuilder.class).sorting();

    private Omitting omitting = this.getClass().getAnnotation(AsXMLByBuilder.class).ommiting();

    @Override
    public Omitting ommiting() {
        return this.omitting;
    }

    @Override
    public void setOmittingOption(Omitting ommiting) {
        this.omitting = ommiting;
    }

    @Override
    public void setSortingOption(Sorting sorting) {
        this.sorting = sorting;
    }

    @Override
    public Sorting sorting() {
        return this.sorting;
    }

}
