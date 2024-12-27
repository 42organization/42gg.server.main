package gg.calendar.api.user.schedule.privateschedule.controller.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.calendar.type.TechTag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PrivateScheduleUpdateReqDto {
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
	public PrivateScheduleUpdateReqDto(EventTag eventTag, JobTag jobTag, TechTag techTag, String title, String content,
		String link, ScheduleStatus status, LocalDateTime startTime, LocalDateTime endTime, boolean alarm,
		Long groupId) {
		this.eventTag = eventTag;
		this.jobTag = jobTag;
		this.techTag = techTag;
		this.title = title;
		this.content = content;
		this.link = link;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
		this.alarm = alarm;
		this.groupId = groupId;
	}
}
