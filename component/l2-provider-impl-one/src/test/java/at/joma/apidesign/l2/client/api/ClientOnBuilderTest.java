package at.joma.apidesign.l2.client.api;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.f.Builder;
import at.joma.apidesign.component.l2.client.api.f.Builder.IL2ComponentInjectionProxy;
import at.joma.apidesign.component.l2.provider.api.AsXML;
import at.joma.apidesign.component.l2.provider.impl.ComponentProducer;

@RunWith(CdiRunner.class)
@AdditionalClasses({
    ComponentProducer.class,
    IL2ComponentInjectionProxy.class,
    Builder.class,
})
public class ClientOnBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClientOnBuilderTest.class);

    @Rule
    public TestRule getWatchman() {
        return new TestWatcher() {

            protected void starting(Description description) {
                LOG.info("*********** Running {} ***********", description.getMethodName());
            }
        };
    }

    @SuppressWarnings("serial")
    public static final AnnotationLiteral<AsXML> AsXml_ANNLIT = new AnnotationLiteral<AsXML>() {
    };
    
    @Test
    public void targetInjection() {
        final BeanManager manager = CDI.current().getBeanManager();

        Bean<Builder> bean = (Bean<Builder>) manager.resolve(manager.getBeans(Builder.class));
        AnnotatedType<Builder> type = manager.createAnnotatedType(Builder.class);

        InjectionTarget<Builder> target = manager.getInjectionTargetFactory(type).createInjectionTarget(bean);
        CreationalContext<Builder> ctx = manager.createCreationalContext(bean);

        Builder builder = target.produce(ctx);
        target.inject(builder, ctx);
        
        IL1Component result = builder.with(AsXml_ANNLIT).build();
    }

    @Test
    public void testComponentDefault_OnBuilder() {

        Builder builder = new Builder();
        IL1Component bean = builder.with(AsXml_ANNLIT).build();

        Assert.assertNotNull(bean);
    }

}
