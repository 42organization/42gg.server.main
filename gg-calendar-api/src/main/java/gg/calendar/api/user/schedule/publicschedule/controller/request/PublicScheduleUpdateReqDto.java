package gg.calendar.api.user.schedule.publicschedule.controller.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.calendar.type.TechTag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicScheduleUpdateReqDto {
	@NotNull
	private DetailClassification classification;
	private EventTag eventTag;
	private JobTag jobTag;
	private TechTag techTag;
	@NotBlank
	private String author;

	@NotBlank
	@Size(max = 50, message = "제목은 50자이하로 입력해주세요.")
	private String title;

	@Size(max = 2000, message = "내용은 2000자이하로 입력해주세요.")
	private String content;
	private String link;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime startTime;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime endTime;
	private ScheduleStatus status;
}
