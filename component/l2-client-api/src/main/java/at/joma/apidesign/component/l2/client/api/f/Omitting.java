package at.joma.apidesign.component.l2.client.api.f;

public @interface Omitting {

	String[] globalFields() default { "globalA", "globalB" };
}
