package at.joma.apidesign.client;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.joma.apidesign.component.impl.ComponentImpl;
import at.joma.apidesign.component.l1.client.api.IL1Component;

@RunWith(CdiRunner.class)
@AdditionalClasses({ ComponentImpl.class })
public class ClientTest {

	@Inject
	IL1Component component;

	@Test
	public void testComponent() {
		component.serialize(null);
	}

}
