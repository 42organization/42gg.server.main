package gg.data.calendar.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DetailClassification {
	EVENT("42행사"),

	JOB_NOTICE("취업공고"),

	PRIVATE_SCHEDULE("개인일정");

	private final String value;
}
