package gg.calendar.api.schedule.privateschedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.auth.UserDto;
import gg.calendar.api.schedule.privateschedule.controller.request.PrivateScheduleCreateReqDto;
import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.PublicSchedule;
import gg.data.user.User;
import gg.repo.calendar.PrivateScheduleRepository;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.user.UserRepository;
import gg.utils.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import gg.calendar.api.schedule.privateschedule.controller.request.PrivateScheduleUpdateReqDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrivateScheduleService {

	private final PrivateScheduleRepository privateScheduleRepository;

	private final PublicScheduleRepository publicScheduleRepository;
  
  private final UserRepository userRepository;

  @Transactional
	public void createPrivateSchedule(UserDto userDto, PrivateScheduleCreateReqDto privateScheduleCreateReqDto) {
		PublicSchedule publicSchedule = PrivateScheduleCreateReqDto.of(userDto.getIntraId(),
			privateScheduleCreateReqDto);
		publicScheduleRepository.save(publicSchedule);
		User user = userRepository.findById(userDto.getId()).orElseThrow(UserNotFoundException::new);
		PrivateSchedule privateSchedule = PrivateSchedule.of(user, publicSchedule,
			privateScheduleCreateReqDto.getColor());
		privateScheduleRepository.save(privateSchedule);
  }
  
	// Todo: 커스텀 에러 처리해야함
	public PrivateSchedule updatePrivateSchedule(Long scheduleId, Long userId,
		PrivateScheduleUpdateReqDto privateScheduleUpdateReqDto) {
		if (privateScheduleUpdateReqDto.getStartTime().isAfter(privateScheduleUpdateReqDto.getEndTime())) {
			throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
		}
		if (privateScheduleUpdateReqDto.getEndTime().isBefore(privateScheduleUpdateReqDto.getStartTime())) {
			throw new IllegalArgumentException("종료 시간이 시작 시간보다 빠를 수 없습니다.");
		}
		if (privateScheduleUpdateReqDto.getEndTime().isEqual(privateScheduleUpdateReqDto.getStartTime())) {
			throw new IllegalArgumentException("시작 시간과 종료 시간이 같을 수 없습니다.");
		}
		if (privateScheduleUpdateReqDto.getDetailClassification() != DetailClassification.NONE) {
			throw new IllegalArgumentException("개인 일정이 아닙니다.");
		}
		PrivateSchedule privateSchedule = privateScheduleRepository.findByUserIdAndScheduleId(userId, scheduleId)
			.orElseThrow(() -> new IllegalArgumentException("해당 일정이 없습니다."));
		privateSchedule.update(privateScheduleUpdateReqDto.getDetailClassification(),
			privateScheduleUpdateReqDto.getTags(),
			privateScheduleUpdateReqDto.getTitle(), privateScheduleUpdateReqDto.getContent(),
			privateScheduleUpdateReqDto.getLink(),
			privateScheduleUpdateReqDto.getStartTime(), privateScheduleUpdateReqDto.getEndTime(),
			privateScheduleUpdateReqDto.isAlarm(),
			privateScheduleUpdateReqDto.getColor());
		privateScheduleRepository.save(privateSchedule);
		publicScheduleRepository.save(privateSchedule.getPublicSchedule());
		return privateSchedule;
	}
}
