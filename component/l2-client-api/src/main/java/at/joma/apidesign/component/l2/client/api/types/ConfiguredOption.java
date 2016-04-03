package at.joma.apidesign.component.l2.client.api.types;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.joma.apidesign.component.l1.client.api.IConfiguredOption;

public class ConfiguredOption implements IConfiguredOption {

	public enum OptionType {
		ENUM(Enum.class), STRINGARRAY(String[].class);

		final Class<?> type;

		OptionType(final Class<?> type) {
			this.type = type;
		}

		public Class<?> getType() {
			return type;
		}
	};

	public ConfiguredOption(Enum<?> option) {
		this.name = option.getClass().getSimpleName();
		this.value = option.name();
		this.type = OptionType.ENUM;
	}

	public ConfiguredOption(String optionName, String[] optionValue) {
		this.name = optionName;
		this.value = Arrays.toString(optionValue);
		this.type = OptionType.STRINGARRAY;
	}

	public final String name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.joma.apidesign.component.l2.client.api.types.IConfiguredOption#getName
	 * ()
	 */
	@Override
	public String getName() {
		return name;
	}

	public final String value;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.joma.apidesign.component.l2.client.api.types.IConfiguredOption#getValue
	 * ()
	 */
	@Override
	public String getValue() {
		return value;
	}

	public final OptionType type;

	public OptionType getType() {
		return type;
	}

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
