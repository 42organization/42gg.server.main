package gg.calendar.api.admin.schedule.totalschedule.controller.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalScheduleAdminSearchListResDto {

	private List<TotalScheduleAdminResDto> totalScheduleAdminResDtoList;

	@Builder
	private TotalScheduleAdminSearchListResDto(List<TotalScheduleAdminResDto> schedules) {
		this.totalScheduleAdminResDtoList = schedules;
	}
}
