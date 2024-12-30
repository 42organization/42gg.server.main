package gg.calendar.api.user.schedule.publicschedule.controller.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import gg.data.calendar.PublicSchedule;
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
public class PublicScheduleCreateReqDto {
	@NotNull
	private DetailClassification classification;
	private EventTag eventTag;
	private JobTag jobTag;
	private TechTag techTag;
	@NotNull
	private String author;

	@NotBlank
	@Size(max = 50, message = "제목은 50자이하로 입력해주세요.")
	private String title;

	@Size(max = 2000, message = "내용은 2000자이하로 입력해주세요.")
	private String content;

	private String link;

	@NotNull
	private LocalDateTime startTime;
	@NotNull
	private LocalDateTime endTime;

	public static PublicSchedule toEntity(String intraId, PublicScheduleCreateReqDto dto) {
		return PublicSchedule.builder()
			.classification(dto.classification)
			.eventTag(dto.eventTag)
			.jobTag(dto.jobTag)
			.techTag(dto.techTag)
			.author(intraId)
			.title(dto.title)
			.content(dto.content)
			.link(dto.link)
			.status(ScheduleStatus.ACTIVATE)
			.startTime(dto.startTime)
			.endTime(dto.endTime)
			.build();
	}
}
