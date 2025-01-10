package gg.calendar.api.admin.util.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.util.controller.response.FortyTwoEventsResponse;
import gg.data.calendar.PublicSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FortyTwoEventsRegisterService {
	private final PublicScheduleAdminRepository publicScheduleAdminRepository;

	@Transactional
	public void registerFortyTwoEvents(List<FortyTwoEventsResponse> events) {
		for (FortyTwoEventsResponse event : events) {
			Optional<PublicSchedule> ps = publicScheduleAdminRepository.findByTitleAndCreatedAtBetween(
				event.getName(),
				event.getCreatedAt().minusSeconds(1),
				event.getCreatedAt().plusSeconds(1)
			);
			if (ps.isEmpty()) {
				publicScheduleAdminRepository.save(event.toPublicSchedule());
			}
		}
	}
}
