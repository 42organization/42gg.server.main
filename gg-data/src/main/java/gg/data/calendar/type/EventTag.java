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

	ETC("기타");

	private final String value;
}
