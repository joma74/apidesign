package at.joma.apidesign.component.l2.client.api.builder.options;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l2.client.api.Omitting;

@Omitting
public class OmittingOptions extends AnnotationLiteral<Omitting> implements Omitting {

	private static final long serialVersionUID = -2363887785491676339L;

	private String[] globalFields = this.getClass().getAnnotation(Omitting.class).globalFields();

	public void setGlobalFields(String[] globalFields) {
		this.globalFields = globalFields;
	}

	@Override
	public String[] globalFields() {
		return globalFields;
	}

}
