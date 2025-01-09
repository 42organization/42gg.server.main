package gg.calendar.api.user.custom.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import gg.data.calendar.ScheduleGroup;
import gg.data.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarCustomCreateReqDto {
	private static final String HEX_COLOR_PATTERN = "^#[A-Fa-f0-9]{6}$";

	@NotBlank
	@Size(max = 50)
	private String title;

	@NotNull
	@Pattern(regexp = HEX_COLOR_PATTERN)
	private String backgroundColor;

	@Builder
	private CalendarCustomCreateReqDto(String title, String backgroundColor) {
		this.title = title;
		this.backgroundColor = backgroundColor;
	}

	public static ScheduleGroup toEntity(User user, CalendarCustomCreateReqDto calendarCustomCreateReqDto) {
		return ScheduleGroup.builder()
			.user(user)
			.title(calendarCustomCreateReqDto.title)
			.backgroundColor(calendarCustomCreateReqDto.backgroundColor)
			.build();
	}
}
