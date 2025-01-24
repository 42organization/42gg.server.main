package gg.calendar.api.user.utils.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.data.calendar.type.ScheduleStatus;
import gg.repo.calendar.PrivateScheduleRepository;
import gg.repo.calendar.PublicScheduleRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleCheckService {
	private final PublicScheduleRepository publicScheduleRepository;
	private final PrivateScheduleRepository privateScheduleRepository;

	public void deactivateExpiredSchedules() {
		publicScheduleRepository.updateExpiredPublicSchedules(ScheduleStatus.DEACTIVATE, ScheduleStatus.ACTIVATE,
			LocalDateTime.now());
		privateScheduleRepository.updateRelatedPrivateSchedules(ScheduleStatus.DEACTIVATE, ScheduleStatus.DEACTIVATE);
	}
}
