package gg.calendar.api.user.schedule.publicschedule;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;


import gg.repo.calendar.PublicScheduleRepository;
import gg.utils.TestDataUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublicScheduleMockData {
	private final EntityManager em;
	private final PublicScheduleRepository publicScheduleRepository;
	private final TestDataUtils testDataUtils;


}
