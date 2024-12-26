package gg.data.calendar.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
public enum JobTag {

	SHORTS_INTERN("체험인턴"),

	INCRUIT_INTERN("채용인턴"),

	NEW_COMER("신입"),

	EXPERIENCED("경력"),

	ETC("기타"),

	NONE(null);

	private final String value;

	public static boolean isValid(String input) {
		if (input == null)
			return true;
		for (JobTag tag : JobTag.values()) {
			if (tag.getValue() != null && tag.getValue().equals(input)) {
				return true;
			}
		}
		return false;
	}

	public static JobTag getJobTag(String input) {
		if (input == null) {
			return NONE;
		}
		for (JobTag tag : JobTag.values()) {
			if (input.equals(tag.getValue())) {
				return tag;
			}
		}
		return NONE;
	}
}
