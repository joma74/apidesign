package at.joma.apidesign.component.l2.provider.api;

import java.lang.annotation.Annotation;

import javax.enterprise.util.AnnotationLiteral;

@AsXML
public class AsXMLOptions extends AnnotationLiteral<AsXML> implements AsXML {

    private static final long serialVersionUID = -3254824763865440793L;

    private Class<? extends Annotation> inScope = this.getClass().getAnnotation(AsXML.class).inScope();

    @Override
    public Class<? extends Annotation> inScope() {
        return this.inScope;
    }
}
