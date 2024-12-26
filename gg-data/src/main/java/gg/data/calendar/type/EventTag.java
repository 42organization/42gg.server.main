package gg.data.calendar.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventTag {
	OFFICIAL_EVENT("재단행사"),

	WENDS_FORUM("수요지식회"),

	JOB_FORUM("취업설명회"),

	INSTRUCTION("강연"),

	ETC("기타"),

	NONE(null);

	private final String value;

	public static boolean isValid(String input) {
		if (input == null)
			return true;
		for (EventTag tag : EventTag.values()) {
			if (tag.getValue() != null && tag.getValue().equals(input)) {
				return true;
			}
		}
		return false;
	}

	public static EventTag getEventTag(String input) {
		if (input == null) {
			return NONE;
		}
		for (EventTag tag : EventTag.values()) {
			if (input.equals(tag.getValue())) {
				return tag;
			}
		}
		return NONE;
	}
}
