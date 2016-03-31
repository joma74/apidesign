package at.joma.apidesign.l2.client.api;

import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.f.Sorting;
import at.joma.apidesign.component.l2.client.api.f.sorting.Order;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;

@RunWith(CdiRunner.class)
public class ClientTest {

	@Inject
	@Sorting
	IL2Component componentDefault;

	@Inject
	@Sorting(order = Order.ALPHABETICALLY, direction = SortingDirection.ASC)
	IL2Component componentAlphaAsc;

	@Test(expected = org.jboss.weld.exceptions.DeploymentException.class)
	public void testComponentDefault() {
		String actual = componentDefault.serialize(null);
	}

	@Test(expected = org.jboss.weld.exceptions.DeploymentException.class)
	public void testComponentAlphaAsc() {
		String actual = componentAlphaAsc.serialize(null);
	}

}
