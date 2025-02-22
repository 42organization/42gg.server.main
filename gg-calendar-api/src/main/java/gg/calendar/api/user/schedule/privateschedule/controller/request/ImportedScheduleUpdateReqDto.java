package gg.calendar.api.user.schedule.privateschedule.controller.request;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImportedScheduleUpdateReqDto {
	@NotNull
	private boolean alarm;

	@NotNull
	private Long groupId;

	@Builder
	private ImportedScheduleUpdateReqDto(boolean alarm, Long groupId) {
		this.alarm = alarm;
		this.groupId = groupId;
	}
}
