package gg.data.calendar.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScheduleStatus {
	ACTIVATE("활성"),
	DEACTIVATE("비활성"),
	DELETE("삭제");

	private final String value;
}
