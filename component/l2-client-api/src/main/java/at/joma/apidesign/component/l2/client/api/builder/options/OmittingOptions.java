package at.joma.apidesign.component.l2.client.api.builder.options;

import java.lang.annotation.Annotation;

import javax.enterprise.util.AnnotationLiteral;

import at.joma.apidesign.component.l2.client.api.Omitting;

@Omitting
public class OmittingOptions extends AnnotationLiteral<Omitting> implements Omitting {

	private static final long serialVersionUID = -2363887785491676339L;

	private String[] byFieldNames = this.getClass().getAnnotation(Omitting.class).byFieldNames();

	private Class<? extends Annotation>[] byFieldAnnotations = this.getClass().getAnnotation(Omitting.class).byFieldAnnotations();

	private Class<?>[] byFieldClasses = this.getClass().getAnnotation(Omitting.class).byFieldClasses();

	@Override
	public String[] byFieldNames() {
		return this.byFieldNames;
	}

	public void setByFieldNames(String[] byFieldNames) {
		this.byFieldNames = byFieldNames;
	}

	@Override
	public Class<? extends Annotation>[] byFieldAnnotations() {
		return this.byFieldAnnotations;
	}

	public void setByFieldAnnotations(Class<? extends Annotation>[] byFieldAnnotations) {
		this.byFieldAnnotations = byFieldAnnotations;
	}

	@Override
	public Class<?>[] byFieldClasses() {
		return this.byFieldClasses;
	}

	public void setByFieldClasses(Class<?>[] byFieldClasses) {
		this.byFieldClasses = byFieldClasses;
	}

}
