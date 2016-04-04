package at.joma.apidesign.component.l2.provider;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;


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

		assertThat(configuredOptions.size(), is(3));
		
		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(not(SortingOrder.ALPHABETICALLY)));

		assertThat(configuredOptions.getValueFor(SortingDirection.class), is(SortingDirection.DESC));
		assertThat(configuredOptions.getValueFor(SortingDirection.class), is(not(SortingDirection.ASC)));
		assertThat(configuredOptions.getValueFor(SortingDirection.class), is(not(SortingDirection.NONE)));

		assertThat((String[]) configuredOptions.getValueFor(GLOBALFIELDS_OPTIONNAME), is(GLOBALFIELDS));
	}
	
	@Test
	public void testTwoEqualConfigurationsAreEqual() {

		ConfiguredOptionsHolder configuredOptions_a = setup_ConfigOptionsHolder_1();
		ConfiguredOptionsHolder configuredOptions_b = setup_ConfigOptionsHolder_1();

		assertThat(configuredOptions_a, is(equalTo(configuredOptions_b)));
	}
	
	@Test
	public void testConfigurationOnSecondTime() {

		ConfiguredOptionsHolder configuredOptions = setup_ConfigOptionsHolder_1();

		assertThat(configuredOptions.size(), is(3));
		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
		assertThat(configuredOptions.getValueFor(SortingDirection.class), is(SortingDirection.DESC));
		assertThat((String[]) configuredOptions.getValueFor(GLOBALFIELDS_OPTIONNAME), is(GLOBALFIELDS));
		
		configuredOptions.with(SortingOrder.ALPHABETICALLY);
		configuredOptions.with(SortingDirection.DESC);
		String[] globalfields_child = new String[] { "_child" };
		configuredOptions.with(GLOBALFIELDS_OPTIONNAME, globalfields_child);
		
		assertThat(configuredOptions.size(), is(3));
		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.ALPHABETICALLY));
		assertThat(configuredOptions.getValueFor(SortingDirection.class), is(SortingDirection.DESC));
		assertThat((String[]) configuredOptions.getValueFor(GLOBALFIELDS_OPTIONNAME), is(globalfields_child));
	}

	@Test
	public void testConfigurationNoDirection() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(ConfiguredOptionsHolder.MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + SortingDirection.class.getSimpleName());

		ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
		assertThat(configuredOptions.size(), is(0));
		
		configuredOptions.with(SortingOrder.GIVEN);

		assertThat(configuredOptions.size(), is(1));
		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(not(SortingOrder.ALPHABETICALLY)));

		configuredOptions.getValueFor(SortingDirection.class);
	}
	
	@Test
	public void testConfigurationNoGlobal() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(ConfiguredOptionsHolder.MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + GLOBALFIELDS_OPTIONNAME);

		ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
		assertThat(configuredOptions.size(), is(0));
		
		configuredOptions.with(SortingOrder.GIVEN);
		
		assertThat(configuredOptions.size(), is(1));
		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
		assertThat(configuredOptions.getValueFor(SortingOrder.class), is(not(SortingOrder.ALPHABETICALLY)));

		configuredOptions.getValueFor(GLOBALFIELDS_OPTIONNAME);
	}

}
