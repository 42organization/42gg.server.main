package gg.calendar.api.schedule.publicschedule.controller.request;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.Tag;
import gg.data.calendar.type.DetailClassification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicScheduleUpdateReqDto {
	@NotNull
	private DetailClassification classification;

	@NotNull
	private Set<Tag> tags;

	@NotBlank
	@Size(max = 50, message = "제목은 50자이하로 입력해주세요.")
	private String title;

	@Size(max = 2000, message = "내용은 2000자이하로 입력해주세요.")
	private String content;

	private String link;

	@NotNull
	private boolean alarm;

	private String color;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime startTime;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime endTime;


}
