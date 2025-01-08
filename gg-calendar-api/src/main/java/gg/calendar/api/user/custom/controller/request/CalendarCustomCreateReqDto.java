package gg.calendar.api.user.custom.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarCustomCreateReqDto {
	private static final String HEX_COLOR_PATTERN = "^#[A-Fa-f0-9]{6}$";

	@NotBlank
	private String title;

	@NotNull
	@Pattern(regexp = HEX_COLOR_PATTERN)
	private String backgroundColor;
}
