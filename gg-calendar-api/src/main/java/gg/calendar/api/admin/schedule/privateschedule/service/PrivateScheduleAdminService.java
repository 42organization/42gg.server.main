package gg.calendar.api.admin.schedule.privateschedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.admin.repo.calendar.PrivateScheduleAdminRepository;
import gg.admin.repo.calendar.ScheduleGroupAdminRepository;
import gg.calendar.api.admin.schedule.privateschedule.controller.response.PrivateScheduleAdminDetailResDto;
import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.ScheduleGroup;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.NotExistException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateScheduleAdminService {

	private final PrivateScheduleAdminRepository privateScheduleAdminRepository;

	private final ScheduleGroupAdminRepository scheduleGroupAdminRepository;

	public PrivateScheduleAdminDetailResDto detailPrivateSchedule(Long id) {
		PrivateSchedule privateSchedule = privateScheduleAdminRepository.findById(id)
			.orElseThrow(() -> new NotExistException(ErrorCode.PRIVATE_SCHEDULE_NOT_FOUND));
		ScheduleGroup scheduleGroup = scheduleGroupAdminRepository.findByScheduleGroupId(
				privateSchedule.getGroupId())
			.orElseThrow(() -> new NotExistException(ErrorCode.SCHEDULE_GROUP_NOT_FOUND));
		return PrivateScheduleAdminDetailResDto.toDto(privateSchedule, scheduleGroup);
	}
}
