package at.joma.apidesign.component.l2.client.api.types.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.helpers.MessageFormatter;

import at.joma.apidesign.component.l2.client.api.types.SortingDirection;

public class OptionTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testOptionSupportForEnum() {
        Option option = new Option(SortingDirection.ASC);
        assertThat(option.getType(), equalTo(Option.OptionType.ENUM));
        assertThat(option.getName(), equalTo(SortingDirection.class.getSimpleName()));
        assertThat(option.getValue(), equalTo(SortingDirection.ASC.name()));
        assertThat(option.toString(), equalTo("Option[SortingDirection, ASC]"));
    }

    @Test
    public void testOptionSupportForSingleString() {
        String OPTION_NAME = "OPTION_NAME";
        String OPTION_VALUE = "OPTION_VALUE";
        Option option = new Option(OPTION_NAME, OPTION_VALUE);
        assertThat(option.getType(), equalTo(Option.OptionType.STRING));
        assertThat(option.getName(), equalTo(OPTION_NAME));
        assertThat(option.getValue(), equalTo(OPTION_VALUE));
        assertThat(option.toString(), equalTo("Option[" + OPTION_NAME + ", " + OPTION_VALUE + "]"));
    }

    @Test
    public void testOptionSupportForArrayString() {
        String OPTION_NAME = "OPTION_NAME";
        String[] OPTION_VALUE = new String[]{
            "OPTION_VALUE_1",
            "OPTION_VALUE_2"
        };
        Option option = new Option(OPTION_NAME, OPTION_VALUE);
        assertThat(option.getType(), equalTo(Option.OptionType.STRINGARRAY));
        assertThat(option.getName(), equalTo(OPTION_NAME));
        assertThat(option.convertValueToArray(), equalTo(OPTION_VALUE));
        assertThat(option.toString(), equalTo("Option[" + OPTION_NAME + ", " + Arrays.toString(OPTION_VALUE) + "]"));
    }
    
    
    @Test
    public void testOptionSupportForArrayClass() {
        String OPTION_NAME = "OPTION_NAME";
        Class<?>[] OPTION_VALUE = new Class[]{
            Object.class,
        };
        Option option = new Option(OPTION_NAME, OPTION_VALUE);
        assertThat(option.getType(), equalTo(Option.OptionType.CLAZZARRAY));
        assertThat(option.getName(), equalTo(OPTION_NAME));
        assertThat(option.convertValueToArray(), equalTo(new String[]{Object.class.getName()}));
        assertThat(option.toString(), equalTo("Option[" + OPTION_NAME + ", [" + Object.class.getName() + "]]"));
        
    }
    
    @Test
    public void testOptionEmptyArrayString() {
        String OPTION_NAME = "OPTION_NAME";
        String[] OPTION_VALUE = new String[]{};
        Option option = new Option(OPTION_NAME, OPTION_VALUE);
        assertThat(option.getType(), equalTo(Option.OptionType.STRINGARRAY));
        assertThat(option.getName(), equalTo(OPTION_NAME));
        assertThat(option.convertValueToArray(), equalTo(OPTION_VALUE));
        assertThat(option.toString(), equalTo("Option[" + OPTION_NAME + ", " + Arrays.toString(OPTION_VALUE) + "]"));
    }
    
    @Test
    public void testOptionEmptyArrayClass() {
        String OPTION_NAME = "OPTION_NAME";
        Class<?>[] OPTION_VALUE = new Class[]{};
        Option option = new Option(OPTION_NAME, OPTION_VALUE);
        assertThat(option.getType(), equalTo(Option.OptionType.CLAZZARRAY));
        assertThat(option.getName(), equalTo(OPTION_NAME));
        assertThat(option.convertValueToArray(), equalTo(new String[]{}));
        assertThat(option.toString(), equalTo("Option[" + OPTION_NAME + ", []]"));
    }
    
    @Test
    public void testOptionNullArrayString() {
        
        String OPTION_NAME = "OPTION_NAME";
        String[] OPTION_VALUE = new String[1];
        
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(MessageFormatter.format(Option.ERR_OPTIONVALUES_NULL, Arrays.toString(OPTION_VALUE), OPTION_NAME).getMessage());
        
        new Option(OPTION_NAME, OPTION_VALUE);
    }
    
    @Test
    public void testOptionNullArrayClass() {
        
        String OPTION_NAME = "OPTION_NAME";
        Class<?>[] OPTION_VALUE = new Class[1];
        
        thrown.expect(NullPointerException.class);
        thrown.expectMessage(MessageFormatter.format(Option.ERR_OPTIONVALUES_NULL, Arrays.toString(OPTION_VALUE), OPTION_NAME).getMessage());
        
        new Option(OPTION_NAME, OPTION_VALUE);
    }

}
