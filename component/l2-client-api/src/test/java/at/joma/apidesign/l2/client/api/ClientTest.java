package at.joma.apidesign.l2.client.api;

import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.f.Omitting;
import at.joma.apidesign.component.l2.client.api.f.Sorting;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;

@RunWith(CdiRunner.class)
public class ClientTest {

	@Inject
	@Sorting
	IL2Component componentDefault;

	@Inject
	@Sorting(order = SortingOrder.GIVEN, direction = SortingDirection.NONE)
	@Omitting(globalFields={"globalTest"})
	IL2Component componentGiven;

	@Test(expected = org.jboss.weld.exceptions.DeploymentException.class)
	public void testComponentDefault_OnCDI() {
		String actual = componentDefault.serialize(null);
	}

	@Test(expected = org.jboss.weld.exceptions.DeploymentException.class)
	public void testComponentGiven_OnCDI() {
		String actual = componentGiven.serialize(null);
	}

}
