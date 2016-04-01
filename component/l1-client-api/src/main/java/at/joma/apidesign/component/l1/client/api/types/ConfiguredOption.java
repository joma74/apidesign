package at.joma.apidesign.component.l1.client.api.types;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ConfiguredOption {

	public ConfiguredOption(Enum<?> option) {
		this.name = option.getClass().getSimpleName();
		this.value = option.name();
	}

	public ConfiguredOption(String name, String[] option) {
		this.name = option.getClass().getSimpleName();
		this.value = Arrays.toString(option);
	}

	public String name;

	public String value;

	public String[] convertValueToArray() {

		String joinedMinusBrackets = value.substring(1, value.length() - 1);

		return joinedMinusBrackets.split(", ");

	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
