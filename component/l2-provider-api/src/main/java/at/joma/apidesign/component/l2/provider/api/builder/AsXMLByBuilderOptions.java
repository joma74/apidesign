package at.joma.apidesign.component.l2.provider.api.builder;

import java.lang.annotation.Annotation;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.builder.IRequiredProviderOptions;

@AsXMLByBuilder
public class AsXMLByBuilderOptions extends AnnotationLiteral<AsXMLByBuilder> implements AsXMLByBuilder, IRequiredProviderOptions {

    private static final long serialVersionUID = -3254824763865440793L;

    private Sorting sorting = this.getClass().getAnnotation(AsXMLByBuilder.class).sorting();

    private Omitting omitting = this.getClass().getAnnotation(AsXMLByBuilder.class).ommiting();

    private Class<? extends Annotation> inScope = this.getClass().getAnnotation(AsXMLByBuilder.class).inScope();

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
    public void setInScope(Class<? extends Annotation> inScope) {
        this.inScope = inScope;
    }

    @Override
    public Sorting sorting() {
        return this.sorting;
    }

    @Override
    public Class<? extends Annotation> inScope() {
        return inScope;
    }

}
