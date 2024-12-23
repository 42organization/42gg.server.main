package gg.calendar.api.user.schedule.publicschedule.controller.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
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
	@NotNull
	private String title;
	private String content;
	private String link;
	@NotNull
	private Integer sharedCount;
	@NotNull
	private LocalDateTime startTime;
	@NotNull
	private LocalDateTime endTime;

	public PublicSchedule of(String userID) {
		return PublicSchedule.builder()
			.classification(classification)
			.eventTag(eventTag)
			.jobTag(jobTag)
			.techTag(techTag)
			.author(userID)
			.title(title)
			.content(content)
			.link(link)
			.sharedCount(sharedCount)
			.startTime(startTime)
			.endTime(endTime)
			.build();
	}
}
