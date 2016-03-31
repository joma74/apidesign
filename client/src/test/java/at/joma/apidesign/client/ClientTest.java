package at.joma.apidesign.client;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.joma.apidesign.component.api.IComponent;
import at.joma.apidesign.component.impl.ComponentImpl;

@RunWith(CdiRunner.class)
@AdditionalClasses({ ComponentImpl.class })
public class ClientTest {

	@Inject
	IComponent component;

	@Test
	public void testComponent() {
		component.serialize(null);
	}

}
