package gg.calendar.api.user.schedule.publicschedule.controller.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

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
public class PublicScheduleUpdateReqDto {
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
	private LocalDateTime startTime;
	@NotNull
	private LocalDateTime endTime;
}
