package gg.pingpong.api.global.scheduler;

import org.springframework.stereotype.Component;

import gg.calendar.api.user.fortytwoevent.service.FortyTwoEventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FortyTwoEventScheduler extends AbstractScheduler {
	private final FortyTwoEventService fortyTwoEventService;

	public FortyTwoEventScheduler(FortyTwoEventService fortyTwoEventService) {
		this.fortyTwoEventService = fortyTwoEventService;
		// this.setCron("0 0 0 * * *");
		this.setCron("0 * * * * *");
	}

	@Override
	public Runnable runnable() {
		return () -> {
			log.info("FortyTwo Event Scheduler Started");
			fortyTwoEventService.checkEvent();
			log.info("getEvents() method was called successfully.");
		};
	}
}
