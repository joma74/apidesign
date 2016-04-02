package at.joma.apidesign.component.l2.provider.api.builder;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.Sorting;
import at.joma.apidesign.component.l2.client.api.builder.IProviderRequiredOptions;

@AsXMLByBuilder
public class AsXMLWithOptions extends AnnotationLiteral<AsXMLByBuilder> implements AsXMLByBuilder, IProviderRequiredOptions {

	private static final long serialVersionUID = -3254824763865440793L;

	private Sorting sorting = this.getClass().getAnnotation(AsXMLByBuilder.class).sorting();

	private Omitting omitting = this.getClass().getAnnotation(AsXMLByBuilder.class).ommiting();

	public void setSortingOption(Sorting sorting) {
		this.sorting = sorting;
	}

	public void setOmittingOption(Omitting ommiting) {
		this.omitting = ommiting;
	}

	@Override
	public Sorting sorting() {
		return sorting;
	}

	@Override
	public Omitting ommiting() {
		return omitting;
	}

}
