package at.joma.stackoverflow.cdi;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.InRequestScope;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
@AdditionalClasses({ Producer.class, SomeCache.class })
public class ExpectedTest {

	@Inject
	Product productA;

	@Inject
	Product productB;

	@Test
	@InRequestScope
	@Ignore("http://stackoverflow.com/questions/36730259/expected-cdi-request-scoped-bean-injected-into-producer-injected-into-dependent")
	public void testScoping() {
		Assert.assertNotNull(productA);
		System.out.println(productA.value);
		Assert.assertNotNull(productB);
	}

}
