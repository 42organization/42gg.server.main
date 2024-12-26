package gg.data.calendar.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
