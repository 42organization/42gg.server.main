package gg.calendar.api.schedule.publicschedule.service;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.auth.UserDto;
import gg.calendar.api.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.calendar.api.schedule.publicschedule.controller.request.PublicScheduleUpdateReqDto;
import gg.calendar.api.schedule.publicschedule.controller.response.PublicScheduleUpdateResDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.user.User;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.user.UserRepository;
import gg.utils.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicScheduleService {
	private final PublicScheduleRepository publicSchduleRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createPublicSchedule(UserDto userDto, PublicScheduleCreateReqDto publicScheduleCreateReqDto)
	{
		PublicSchedule publicSchedule = PublicScheduleCreateReqDto.of(userDto.getIntraId(),
			publicScheduleCreateReqDto);
		publicSchduleRepository.save(publicSchedule);
	}

	//TODO: 에러처리하기!
	@Transactional
	public PublicScheduleUpdateResDto updatePublicSchedule(Long scheduleId, Long userId, PublicScheduleUpdateReqDto publicScheduleUpdateReqDto){
		PublicSchedule publicSchedule = publicSchduleRepository.findByUserIdAndSchduleId(userId, scheduleId).orElseThrow(()-> new IllegalArgumentException("해당 일정이 없습니다."));
		publicSchedule.update(publicScheduleUpdateReqDto.getClassification(), publicScheduleUpdateReqDto.getTags(),
			publicScheduleUpdateReqDto.getTitle(), publicScheduleUpdateReqDto.getContent(),
			publicScheduleUpdateReqDto.getLink(), publicScheduleUpdateReqDto.getStartTime(),
			publicScheduleUpdateReqDto.getEndTime(), publicScheduleUpdateReqDto.isAlarm(),
			publicScheduleUpdateReqDto.getColor());
		return PublicScheduleUpdateResDto.from(publicSchedule);
	}

	// TODO: 에러처리하기
	@Transactional
	public void deletePublicSchedule(Long scheduleId, Long userId) {
		PublicSchedule publicSchedule = publicSchduleRepository.findByUserIdAndSchduleId(userId, scheduleId).orElseThrow(()-> new IllegalArgumentException("해당 일정이 없습니다"));

		if (publicSchedule.getStatus() == ScheduleStatus.DELETE) {
			throw new IllegalArgumentException("이미 삭제된 일정입니다.");
		}
		publicSchedule.delete();
	}
}
