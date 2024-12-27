package gg.calendar.api.admin.schedule.publicschedule.controller.response;

import java.time.LocalDateTime;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.calendar.type.TechTag;
import lombok.Getter;

@Getter
public class PublicScheduleAdminSimpleResDto {
	private Long id;

	private DetailClassification classification;

	private EventTag eventTag;

	private JobTag jobTag;

	private TechTag techTag;

	private String author;

	private String title;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private String link;

	private Integer sharedCount;

	private ScheduleStatus status;

	public PublicScheduleAdminSimpleResDto(PublicSchedule publicSchedule) {
		this.id = publicSchedule.getId();
		this.classification = publicSchedule.getClassification();
		this.eventTag = publicSchedule.getEventTag();
		this.jobTag = publicSchedule.getJobTag();
		this.techTag = publicSchedule.getTechTag();
		this.author = publicSchedule.getAuthor();
		this.title = publicSchedule.getTitle();
		this.startTime = publicSchedule.getStartTime();
		this.endTime = publicSchedule.getEndTime();
		this.link = publicSchedule.getLink();
		this.sharedCount = publicSchedule.getSharedCount();
		this.status = publicSchedule.getStatus();

	}
}
