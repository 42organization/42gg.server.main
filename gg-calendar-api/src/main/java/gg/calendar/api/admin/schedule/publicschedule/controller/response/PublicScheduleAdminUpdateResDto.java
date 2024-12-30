package gg.calendar.api.admin.schedule.publicschedule.controller.response;

import java.time.LocalDateTime;

import gg.data.calendar.PublicSchedule;
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
public class PublicScheduleAdminUpdateResDto {

	private Long id;

	private String classification;

	private EventTag eventTag;

	private JobTag jobTag;

	private TechTag techTag;

	private String author;

	private String title;

	private String content;

	private String link;

	private Integer sharedCount;

	private ScheduleStatus status;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	@Builder
	private PublicScheduleAdminUpdateResDto(Long id, String classification, EventTag eventTag, JobTag jobTag,
		TechTag techTag,
		String author, String title, String content, String link, Integer sharedCount, ScheduleStatus status,
		LocalDateTime startTime, LocalDateTime endTime) {
		this.id = id;
		this.classification = classification;
		this.eventTag = eventTag;
		this.jobTag = jobTag;
		this.techTag = techTag;
		this.author = author;
		this.title = title;
		this.content = content;
		this.link = link;
		this.sharedCount = sharedCount;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;

	}

	public static PublicScheduleAdminUpdateResDto toDto(PublicSchedule publicSchedule) {
		return PublicScheduleAdminUpdateResDto.builder()
			.id(publicSchedule.getId())
			.classification(publicSchedule.getClassification().name())
			.eventTag(publicSchedule.getEventTag())
			.jobTag(publicSchedule.getJobTag())
			.techTag(publicSchedule.getTechTag())
			.author(publicSchedule.getAuthor())
			.title(publicSchedule.getTitle())
			.content(publicSchedule.getContent())
			.link(publicSchedule.getLink())
			.sharedCount(publicSchedule.getSharedCount())
			.status(publicSchedule.getStatus())
			.startTime(publicSchedule.getStartTime())
			.endTime(publicSchedule.getEndTime())
			.build();
	}
}
