package gg.calendar.api.admin.schedule.totalschedule.controller.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalScheduleAdminSearchReqDto {

	@NotNull
	private String type;

	private String content;
	
	private LocalDateTime startTime;

	private LocalDateTime endTime;
}
