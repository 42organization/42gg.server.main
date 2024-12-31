package gg.calendar.api.user.schedule.publicschedule.controller.response;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.TechTag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicScheduleDetailRetrieveResDto {
	private Long id;
	private DetailClassification classification;
	private EventTag eventTag;
	private JobTag jobTag;
	private TechTag techTag;
	private String author;
	private String title;
	private String content;
	private String link;
	private String startTime;
	private String endTime;
	private Integer sharedCount;

	@Builder
	private PublicScheduleDetailRetrieveResDto(Long id, DetailClassification classification, EventTag eventTag,
		JobTag jobTag,
		TechTag techTag, String author, String title, String content, String link, String startTime, String endTime,
		Integer sharedCount) {
		this.id = id;
		this.classification = classification;
		this.eventTag = eventTag;
		this.jobTag = jobTag;
		this.techTag = techTag;
		this.author = author;
		this.title = title;
		this.content = content;
		this.link = link;
		this.startTime = startTime;
		this.endTime = endTime;
		this.sharedCount = sharedCount;
	}

	public static PublicScheduleDetailRetrieveResDto toDto(PublicSchedule publicSchedule) {
		return PublicScheduleDetailRetrieveResDto.builder()
			.id(publicSchedule.getId())
			.classification(publicSchedule.getClassification())
			.eventTag(publicSchedule.getEventTag())
			.jobTag(publicSchedule.getJobTag())
			.techTag(publicSchedule.getTechTag())
			.author(publicSchedule.getAuthor())
			.title(publicSchedule.getTitle())
			.content(publicSchedule.getContent())
			.link(publicSchedule.getLink())
			.startTime(publicSchedule.getStartTime().toString())
			.endTime(publicSchedule.getEndTime().toString())
			.sharedCount(publicSchedule.getSharedCount())
			.build();
	}
}
