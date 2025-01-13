package gg.pingpong.api.global.scheduler;

import java.util.List;

import org.springframework.stereotype.Component;

import gg.calendar.api.admin.util.GetFortyTwoEvents;
import gg.calendar.api.admin.util.controller.response.FortyTwoEventsResponse;
import gg.calendar.api.admin.util.service.FortyTwoEventsRegisterService;
import gg.calendar.api.admin.util.service.ScheduleCheckService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventScheduler extends AbstractScheduler {

	private final GetFortyTwoEvents events;

	private final FortyTwoEventsRegisterService fortyTwoEventsRegisterService;

	private final ScheduleCheckService scheduleCheckService;

	public EventScheduler(GetFortyTwoEvents events, FortyTwoEventsRegisterService fortyTwoEventsRegisterService,
		ScheduleCheckService scheduleCheckService) {
		this.events = events;
		this.fortyTwoEventsRegisterService = fortyTwoEventsRegisterService;
		this.scheduleCheckService = scheduleCheckService;
		this.setCron("0 0/1 * * * *");
		// this.setCron("0 0 0 * * *");
	}

	@Override
	public Runnable runnable() {
		return () -> {
			log.info("Set 42 Event List ");
			List<FortyTwoEventsResponse> eventsLists = events.getEvents();
			fortyTwoEventsRegisterService.registerFortyTwoEvents(eventsLists);
			scheduleCheckService.checkSchedule();
		};
	}
}
