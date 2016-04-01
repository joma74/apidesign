package at.joma.apidesign.component.l2.provider.api;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l2.client.api.f.Omitting;
import at.joma.apidesign.component.l2.client.api.f.Sorting;

@AsXML
public class AsXMLQualifier extends AnnotationLiteral<AsXML> implements AsXML {

	private static final long serialVersionUID = -3254824763865440793L;

	@Override
	public Sorting sorting() {
		return this.getClass().getAnnotation(AsXML.class).sorting();
	}

	@Override
	public Omitting ommiting() {
		return this.getClass().getAnnotation(AsXML.class).ommiting();
	}

}
