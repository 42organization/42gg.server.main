package gg.calendar.api.admin.schedule.privateschedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.admin.repo.calendar.PrivateScheduleAdminRepository;
import gg.calendar.api.admin.schedule.privateschedule.controller.response.PrivateScheduleAdminDetailResDto;
import gg.data.calendar.PrivateSchedule;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.NotExistException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateScheduleAdminService {

	private final PrivateScheduleAdminRepository privateScheduleAdminRepository;

	private final

	public PrivateScheduleAdminDetailResDto detailPrivateSchedule(Long id) {

		PrivateSchedule privateSchedule = privateScheduleAdminRepository.findById(id)
			.orElseThrow(() -> new NotExistException(ErrorCode.PRIVATE_SCHEDULE_NOT_FOUND));

		return PrivateScheduleAdminDetailResDto.toDto(privateSchedule);
	}
}
