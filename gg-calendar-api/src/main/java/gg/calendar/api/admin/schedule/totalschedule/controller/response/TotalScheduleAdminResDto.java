package gg.calendar.api.admin.schedule.totalschedule.controller.response;

import java.time.LocalDateTime;

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
public class TotalScheduleAdminResDto {

	private Long id;

	private DetailClassification classification;

	private EventTag eventTag;

	private JobTag jobTag;

	private TechTag techTag;

	private String author;

	private String title;

	private String content;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private String link;

	private Integer sharedCount;

	private ScheduleStatus status;

	@Builder
	public TotalScheduleAdminResDto(PublicSchedule publicSchedule) {
		this.id = publicSchedule.getId();
		this.classification = publicSchedule.getClassification();
		this.eventTag = publicSchedule.getEventTag();
		this.jobTag = publicSchedule.getJobTag();
		this.techTag = publicSchedule.getTechTag();
		this.author = publicSchedule.getAuthor();
		this.title = publicSchedule.getTitle();
		this.content = publicSchedule.getContent();
		this.startTime = publicSchedule.getStartTime();
		this.endTime = publicSchedule.getEndTime();
		this.link = publicSchedule.getLink();
		this.sharedCount = publicSchedule.getSharedCount();
		this.status = publicSchedule.getStatus();
	}

	@Override
	public String toString() {
		return "TotalScheduleAdminResDto [id=" + id + ", classification=" + classification + ", eventTag=" + eventTag
			+ ", jobTag=" + jobTag + ", techTag=" + techTag + ", author=" + author + ", title=" + title
			+ ", content=" + content + ", startTime=" + startTime + ", endTime=" + endTime + ", link=" + link
			+ ", sharedCount=" + sharedCount
			+ ", status=" + status + "]";
	}
}
