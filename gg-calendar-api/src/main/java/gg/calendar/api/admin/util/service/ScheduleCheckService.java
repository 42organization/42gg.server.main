package gg.calendar.api.admin.util.service;

import java.time.LocalDate;
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

	private static final String SCHEDULE_MESSAGE_D_DAY = "일정요정🧚으로부터 알림이 도착했습니다.\n"
		+ "오늘의 일정을 확인해보세요!\n";

	private static final String SCHEDULE_MESSAGE_BEFORE_D_DAY = "일정요정🧚으로부터 알림이 도착했습니다.\n"
		+ "내일의 일정을 확인해보세요!\n";

	private final MessageSender messageSender;

	@Transactional(readOnly = true)
	public void sendScheduleNotifications(User user, String message, PrivateSchedule schedule) {
		String msg = message + "일정 : " + schedule.getPublicSchedule().getTitle() + "\n설명 : "
			+ schedule.getPublicSchedule().getContent() + "\n\n일정 상세 링크 : " + schedule.getPublicSchedule().getLink()
			+ "\n\n캘린더 바로가기 : https://gg.42seoul.kr/calendar";
		messageSender.send(user.getIntraId(), msg);
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
		List<PrivateSchedule> schedules = privateScheduleAdminRepository.findSchedulesWithAlarmForBothDays(startOfDay,
			endOfDay, startOfDay.plusDays(1), endOfDay.plusDays(1), ScheduleStatus.ACTIVATE);
		// 알림보내는 로직
		for (PrivateSchedule schedule : schedules) {
			LocalDateTime endTime = schedule.getPublicSchedule().getEndTime();
			LocalDate today = LocalDate.now();
			LocalDate scheduleDay = endTime.toLocalDate();
			User user = schedule.getUser();
			if (scheduleDay.isEqual(today)) {
				log.info("D-Day Alarm for Schedule: {}", schedule.toString());
				sendScheduleNotifications(user, SCHEDULE_MESSAGE_D_DAY, schedule);
			} else {
				log.info("D-Day-1 Alarm for Schedule: {}", schedule.toString());
				sendScheduleNotifications(user, SCHEDULE_MESSAGE_BEFORE_D_DAY, schedule);
			}
		}

	}
}
