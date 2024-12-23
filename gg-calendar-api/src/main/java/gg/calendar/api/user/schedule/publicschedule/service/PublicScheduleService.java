package gg.calendar.api.user.schedule.publicschedule.service;




import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.data.user.User;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicScheduleService {
	private final PublicScheduleRepository publicScheduleRepository;
	private final UserRepository userRepository;

	@Transactional
	public PublicSchedule createPublicSchedule(PublicScheduleCreateReqDto req, String userId){
		User user = userRepository.findByIntraId(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		if (!userId.equals(req.getAuthor())) {
			throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
		}
		PublicSchedule publicSchedule = req.of(userId);
		return publicScheduleRepository.save(publicSchedule);
	}
}
