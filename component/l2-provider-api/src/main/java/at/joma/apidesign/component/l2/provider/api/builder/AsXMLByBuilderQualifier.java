package at.joma.apidesign.component.l2.provider.api.builder;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l2.client.api.f.IProviderTypeWithOptionsSetter;
import at.joma.apidesign.component.l2.client.api.f.Omitting;
import at.joma.apidesign.component.l2.client.api.f.Sorting;

@AsXMLByBuilder
public class AsXMLByBuilderQualifier extends AnnotationLiteral<AsXMLByBuilder> implements AsXMLByBuilder, IProviderTypeWithOptionsSetter {

	private static final long serialVersionUID = -3254824763865440793L;

	private Sorting sorting = this.getClass().getAnnotation(AsXMLByBuilder.class).sorting();

	private Omitting omitting = this.getClass().getAnnotation(AsXMLByBuilder.class).ommiting();

	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}

	public void setOmitting(Omitting ommiting) {
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
