package gg.calendar.api.admin.util.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.admin.repo.calendar.PrivateScheduleAdminRepository;
import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.user.User;
import gg.utils.sns.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleCheckService {

	private final PublicScheduleAdminRepository publicScheduleAdminRepository;
	private final PrivateScheduleAdminRepository privateScheduleAdminRepository;

	private static final String Schedule_MESSAGE = "파티요정🧚으로부터 편지가 도착했습니다.\n"
		+ "TEST\n";

	private final MessageSender messageSender;

	@Transactional(readOnly = true)
	public void sendScheduleNotifications(User user) {
		messageSender.send(user.getIntraId(), Schedule_MESSAGE);
	}

	@Transactional
	public void checkSchedule() {
		log.info("Check Schedule");

		// 종료된 스케쥴 비활성화
		publicScheduleAdminRepository.updateExpiredPublicSchedules(ScheduleStatus.DEACTIVATE,
			ScheduleStatus.ACTIVATE,
			LocalDateTime.now());

		privateScheduleAdminRepository.updateRelatedPrivateSchedules(ScheduleStatus.DEACTIVATE,
			ScheduleStatus.DEACTIVATE);

		// 디데이
		LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		log.info("Start Of Day : {}", startOfDay);
		log.info("End Of Day : {}", endOfDay);
		List<PrivateSchedule> schedules = privateScheduleAdminRepository.findSchedulesWithAlarmWithStatus(startOfDay,
			endOfDay, ScheduleStatus.ACTIVATE);
		// 알림보내는 로직
		for (PrivateSchedule schedule : schedules) {
			log.info("Send D-Day Alarm Schedule : {}", schedule.toString());
			User user = schedule.getUser();
			sendScheduleNotifications(user);
		}

		List<PrivateSchedule> nextSchedules = privateScheduleAdminRepository.findSchedulesWithAlarmWithStatus(
			startOfDay.plusDays(1), endOfDay.plusDays(1), ScheduleStatus.ACTIVATE);
		// 디데이-1 알림보내는 로직
		for (PrivateSchedule schedule : nextSchedules) {
			log.info("Send D-Day-1 Alarm Schedule : {}", schedule.toString());
			User user = schedule.getUser();
			sendScheduleNotifications(user);
		}
	}
}
