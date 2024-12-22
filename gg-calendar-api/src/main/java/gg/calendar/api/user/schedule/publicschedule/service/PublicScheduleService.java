package gg.calendar.api.user.schedule.publicschedule.service;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.repo.calendar.PublicScheduleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicScheduleService {
	private final PublicScheduleRepository publicScheduleRepository;

	@Transactional
	public PublicSchedule createPublicSchedule(PublicScheduleCreateReqDto req) {
		PublicSchedule publicSchedule = req.of();


		return publicScheduleRepository.save(publicSchedule);
	}
}
