package gg.calendar.api.user.custom.controller.response;

import gg.data.calendar.ScheduleGroup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarCustomViewResDto {
	private Long id;

	private String title;

	private String backgroundColor;

	@Builder
	private CalendarCustomViewResDto(Long id, String title, String backgroundColor) {
		this.id = id;
		this.title = title;
		this.backgroundColor = backgroundColor;
	}

	public static CalendarCustomViewResDto toDto(ScheduleGroup scheduleGroup) {
		return CalendarCustomViewResDto.builder()
			.id(scheduleGroup.getId())
			.title(scheduleGroup.getTitle())
			.backgroundColor(scheduleGroup.getBackgroundColor())
			.build();
	}
}
