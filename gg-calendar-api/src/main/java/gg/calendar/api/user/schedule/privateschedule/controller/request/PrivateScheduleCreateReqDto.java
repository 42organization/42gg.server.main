package gg.calendar.api.user.schedule.privateschedule.controller.request;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateScheduleCreateReqDto {

	@NotNull
	private DetailClassification classification;

	private EventTag eventTag;

	private JobTag jobTag;

	private TechTag techTag;

	@NotBlank
	@Size(max = 50)
	private String title;

	@Size(max = 2000)
	private String content;

	private String link;

	private ScheduleStatus status;

	@NotNull
	private LocalDateTime startTime;

	@NotNull
	private LocalDateTime endTime;

	@NotNull
	private boolean alarm;

	@NotNull
	private Long groupId;

	@Builder
	private PrivateScheduleCreateReqDto(DetailClassification classification, EventTag eventTag, JobTag jobTag,
		TechTag techTag, String title, String content, String link,
		LocalDateTime startTime, LocalDateTime endTime, boolean alarm, Long groupId,
		ScheduleStatus status) {
		this.classification = classification;
		this.eventTag = eventTag;
		this.jobTag = jobTag;
		this.techTag = techTag;
		this.title = title;
		this.content = content;
		this.link = link;
		this.startTime = startTime;
		this.endTime = endTime;
		this.alarm = alarm;
		this.groupId = groupId;
		this.status = status;
	}

	public static PublicSchedule toEntity(String intraId, PrivateScheduleCreateReqDto privateScheduleCreateReqDto) {
		return PublicSchedule.builder()
			.classification(privateScheduleCreateReqDto.classification)
			.eventTag(privateScheduleCreateReqDto.eventTag)
			.jobTag(privateScheduleCreateReqDto.jobTag)
			.techTag(privateScheduleCreateReqDto.techTag)
			.author(intraId).title(privateScheduleCreateReqDto.title)
			.content(privateScheduleCreateReqDto.content)
			.link(privateScheduleCreateReqDto.link)
			.startTime(privateScheduleCreateReqDto.startTime)
			.endTime(privateScheduleCreateReqDto.endTime)
			.status(privateScheduleCreateReqDto.status)
			.build();
	}
}
