package gg.data.calendar.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DetailClassification {
	EVENT("42행사"),

	JOB_NOTICE("취업공고"),

	ETC("개인일정");

	private final String value;

	public static boolean isValid(String input) {
		if (input == null) {
			return false;
		}

		for (DetailClassification tag : DetailClassification.values()) {
			if (tag.getValue().equals(input)) {
				return true;
			}
		}
		return false;
	}

	public static DetailClassification getDetailClassificationTag(String input) {
		for (DetailClassification tag : DetailClassification.values()) {
			if (input.equals(tag.getValue())) {
				return tag;
			}
		}
		return null;
	}
}
