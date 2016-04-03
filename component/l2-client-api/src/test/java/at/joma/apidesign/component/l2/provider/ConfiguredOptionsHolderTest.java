package at.joma.apidesign.component.l2.provider;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import at.joma.apidesign.component.l2.client.api.types.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;

public class ConfiguredOptionsHolderTest {

	public static final String GLOBALFIELDS_OPTIONNAME = "globalFields";
	
	public static final String[] GLOBALFIELDS = new String[] { "_parent" };

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	public ConfiguredOptionsHolder setup_ConfigOptionsHolder_1(){
		ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
		configuredOptions//
				.with(SortingOrder.GIVEN)//
				.with(SortingDirection.DESC)//
				.with(GLOBALFIELDS_OPTIONNAME, GLOBALFIELDS);
		
		return configuredOptions;
	}

	@Test
	public void testConfigurationAllSet() {

		ConfiguredOptionsHolder configuredOptions = setup_ConfigOptionsHolder_1();

		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(not(SortingOrder.ALPHABETICALLY)));

		assertThat(configuredOptions.getValueFor(SortingDirection.class), is(SortingDirection.DESC));
		assertThat(configuredOptions.getValueFor(SortingDirection.class), is(not(SortingDirection.ASC)));
		assertThat(configuredOptions.getValueFor(SortingDirection.class), is(not(SortingDirection.NONE)));

		assertThat((String[]) configuredOptions.getValueFor(GLOBALFIELDS_OPTIONNAME), is(GLOBALFIELDS));
	}

	@Test
	public void testConfigurationNoDirection() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(ConfiguredOptionsHolder.MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + SortingDirection.class.getSimpleName());

		ConfiguredOptionsHolder configuredOptions2 = new ConfiguredOptionsHolder();
		configuredOptions2.with(SortingOrder.GIVEN);

		assertThat(configuredOptions2.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
		assertThat(configuredOptions2.getValueFor(SortingOrder.class), is(not(SortingOrder.ALPHABETICALLY)));

		configuredOptions2.getValueFor(SortingDirection.class);
	}
	
	@Test
	public void testConfigurationNoGlobal() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(ConfiguredOptionsHolder.MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + GLOBALFIELDS_OPTIONNAME);

		ConfiguredOptionsHolder configuredOptions2 = new ConfiguredOptionsHolder();
		configuredOptions2.with(SortingOrder.GIVEN);

		assertThat(configuredOptions2.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
		assertThat(configuredOptions2.getValueFor(SortingOrder.class), is(not(SortingOrder.ALPHABETICALLY)));

		configuredOptions2.getValueFor(GLOBALFIELDS_OPTIONNAME);
	}

}
