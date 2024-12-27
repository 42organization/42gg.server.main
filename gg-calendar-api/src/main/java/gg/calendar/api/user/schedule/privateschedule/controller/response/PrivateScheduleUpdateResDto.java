package gg.calendar.api.user.schedule.privateschedule.controller.response;

import java.time.LocalDateTime;

import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.calendar.type.TechTag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateScheduleUpdateResDto {
	private Long id;

	private DetailClassification classification;

	private EventTag eventTag;

	private JobTag jobTag;

	private TechTag techTag;

	private String title;

	private String content;

	private String link;

	private ScheduleStatus status;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private boolean alarm;

	private Long groupId;
}
