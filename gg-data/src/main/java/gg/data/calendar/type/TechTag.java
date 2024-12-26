package gg.data.calendar.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechTag {

	FRONT_END("FE"),

	BACK_END("BE"),

	DATA("DATA"),

	CLOUD("CLOUD"),

	AI("AI"),

	SERVER("SERVER"),

	NETWORK("NETWORK"),

	ETC("기타"),

	NONE(null);

	private final String value;

	public static boolean isValid(String input) {
		if (input == null)
			return true;
		for (TechTag tag : TechTag.values()) {
			if (tag.getValue() != null && tag.getValue().equals(input)) {
				return true;
			}
		}
		return false;
	}

	public static TechTag getTechTag(String input) {
		if (input == null) {
			return NONE;
		}
		for (TechTag tag : TechTag.values()) {
			if (input.equals(tag.getValue())) {
				return tag;
			}
		}
		return NONE;
	}
}
