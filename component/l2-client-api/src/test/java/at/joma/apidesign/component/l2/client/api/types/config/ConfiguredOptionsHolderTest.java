package at.joma.apidesign.component.l2.client.api.types.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import at.joma.apidesign.component.l2.client.api.Omitting;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;

public class ConfiguredOptionsHolderTest {

    public static final String[] OMITTING_BYFIELDNAMES_1 = new String[]{
        "_parent"
    };
    
    public static final Class<?>[] OMITTING_BYFIELDANNOTATIONS_2 = new Class[]{
        Inject.class
    };
    
    public static final Class<?>[] OMITTING_BYFIELDCLASSES_2 = new Class[]{
        ByFieldClass2.class
    };

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public ConfiguredOptionsHolder setup_ConfigOptionsHolder_1() {
        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        configuredOptions//
                .with(SortingOrder.GIVEN)//
                .with(SortingDirection.DESC)//
                .with(Omitting.BYFIELDNAMES_OPTIONNAME, OMITTING_BYFIELDNAMES_1);

        return configuredOptions;
    }
    
    public ConfiguredOptionsHolder setup_ConfigOptionsHolder_2() {
        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        configuredOptions//
                .with(SortingOrder.ALPHABETICALLY)//
                .with(SortingDirection.ASC)//
                .with(Omitting.BYFIELDNAMES_OPTIONNAME, OMITTING_BYFIELDNAMES_1)
                .with(Omitting.BYFIELDANNOTATIONS_OPTIONNAME, OMITTING_BYFIELDANNOTATIONS_2)
                .with(Omitting.BYFIELDCLASSES_OPTIONNAME, OMITTING_BYFIELDCLASSES_2)
                ;

        return configuredOptions;
    }
    
    @Test
    public void testForSameHashCode() {
        
        ConfiguredOptionsHolder a = setup_ConfigOptionsHolder_1();
        ConfiguredOptionsHolder b = setup_ConfigOptionsHolder_1();

        Assert.assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void test_AllSetable() {

        ConfiguredOptionsHolder configuredOptions = setup_ConfigOptionsHolder_1();

        assertThat(configuredOptions.optionsCount(), is(3));

        assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
        assertThat(configuredOptions.getValueFor(SortingOrder.class), is(not(SortingOrder.ALPHABETICALLY)));

        assertThat(configuredOptions.getValueFor(SortingDirection.class), is(SortingDirection.DESC));
        assertThat(configuredOptions.getValueFor(SortingDirection.class), is(not(SortingDirection.ASC)));
        assertThat(configuredOptions.getValueFor(SortingDirection.class), is(not(SortingDirection.NONE)));

        assertThat((String[]) configuredOptions.getValueFor(Omitting.BYFIELDNAMES_OPTIONNAME), is(OMITTING_BYFIELDNAMES_1));
    }

    @Test
    public void test_Given_LeftOutDirection_When_getValueFor_Then_IllegalArgumentException() {

        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage(ConfiguredOptionsHolder.MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + SortingDirection.class.getSimpleName());

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        assertThat(configuredOptions.optionsCount(), is(0));

        configuredOptions.with(SortingOrder.GIVEN);

        assertThat(configuredOptions.optionsCount(), is(1));
        assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
        assertThat(configuredOptions.getValueFor(SortingOrder.class), is(not(SortingOrder.ALPHABETICALLY)));

        configuredOptions.getValueFor(SortingDirection.class);
    }

    @Test
    public void test_Given_LeftOutGlobal_When_getValueFor_Then_IllegalArgumentException() {

        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage(ConfiguredOptionsHolder.MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR + Omitting.BYFIELDNAMES_OPTIONNAME);

        ConfiguredOptionsHolder configuredOptions = new ConfiguredOptionsHolder();
        assertThat(configuredOptions.optionsCount(), is(0));

        configuredOptions.with(SortingOrder.GIVEN);

        assertThat(configuredOptions.optionsCount(), is(1));
        assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
        assertThat(configuredOptions.getValueFor(SortingOrder.class), is(not(SortingOrder.ALPHABETICALLY)));

        configuredOptions.getValueFor(Omitting.BYFIELDNAMES_OPTIONNAME);
    }

    @Test
    public void test_When_SecondTime_Then_OverwritesFirstTime() {

        ConfiguredOptionsHolder configuredOptions = setup_ConfigOptionsHolder_1();

        assertThat(configuredOptions.optionsCount(), is(3));
        assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.GIVEN));
        assertThat(configuredOptions.getValueFor(SortingDirection.class), is(SortingDirection.DESC));
        assertThat((String[]) configuredOptions.getValueFor(Omitting.BYFIELDNAMES_OPTIONNAME), is(OMITTING_BYFIELDNAMES_1));

        configuredOptions.with(SortingOrder.ALPHABETICALLY);
        configuredOptions.with(SortingDirection.DESC);
        String[] globalfields_child = new String[]{
            "_child"
        };
        configuredOptions.with(Omitting.BYFIELDNAMES_OPTIONNAME, globalfields_child);

        assertThat(configuredOptions.optionsCount(), is(3));
        assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.ALPHABETICALLY));
        assertThat(configuredOptions.getValueFor(SortingDirection.class), is(SortingDirection.DESC));
        assertThat((String[]) configuredOptions.getValueFor(Omitting.BYFIELDNAMES_OPTIONNAME), is(globalfields_child));
    }

    @Test
    public void test_Given_TwoEqualConfigurations_Then_AreEqual() {

        ConfiguredOptionsHolder configuredOptions_a = setup_ConfigOptionsHolder_1();
        ConfiguredOptionsHolder configuredOptions_b = setup_ConfigOptionsHolder_1();

        assertThat(configuredOptions_a, is(equalTo(configuredOptions_b)));
    }
    
    @Test
    public void test_Given_AskEnumValueByString_Then_IllegalArgumentException() {
    	
    	thrown.expect(IllegalArgumentException.class);
    	thrown.expectMessage(ConfiguredOptionsHolder.MESSAGE_FAILURE_ASKCONFIGIUREDENUMOPTIONBYENUM);

        ConfiguredOptionsHolder configuredOptions_a = setup_ConfigOptionsHolder_1();

        configuredOptions_a.getValueFor(SortingOrder.class.getSimpleName());
    }
    
    @Test
    public void test_Given_AskUnkownOption_Then_IllegalArgumentException() {
    	
    	thrown.expect(IllegalArgumentException.class);
    	thrown.expectMessage(ConfiguredOptionsHolder.MESSAGE_FAILURE_NOCONFIGUREDOPTIONFOR);

        ConfiguredOptionsHolder configuredOptions_a = setup_ConfigOptionsHolder_1();

        configuredOptions_a.getValueFor("thisoptionnameisnotknown");
    }
    
    
    @Test
    public void test_GivenArrayClasses_Then_ConversionAndEqualOkay() {
        ConfiguredOptionsHolder configuredOptions = setup_ConfigOptionsHolder_2();
        
        assertThat(configuredOptions.optionsCount(), is(5));
        assertThat(configuredOptions.getValueFor(SortingOrder.class), is(SortingOrder.ALPHABETICALLY));
        assertThat(configuredOptions.getValueFor(SortingDirection.class), is(SortingDirection.ASC));
        assertThat((String[]) configuredOptions.getValueFor(Omitting.BYFIELDNAMES_OPTIONNAME), is(OMITTING_BYFIELDNAMES_1));
        assertThat((Class<?>[]) configuredOptions.getValueFor(Omitting.BYFIELDANNOTATIONS_OPTIONNAME), is(OMITTING_BYFIELDANNOTATIONS_2));
        assertThat((Class<?>[]) configuredOptions.getValueFor(Omitting.BYFIELDCLASSES_OPTIONNAME), is(OMITTING_BYFIELDCLASSES_2));
        
    }

}
