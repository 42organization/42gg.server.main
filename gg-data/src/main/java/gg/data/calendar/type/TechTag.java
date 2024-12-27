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

	ETC("기타");

	private final String value;
}
