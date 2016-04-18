package at.joma.apidesign.component.l2.client.api.types.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import org.junit.Test;

import at.joma.apidesign.component.l2.client.api.types.SortingDirection;

public class OptionTest {

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

}
