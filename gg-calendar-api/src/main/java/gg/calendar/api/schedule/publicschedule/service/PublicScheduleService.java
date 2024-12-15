package gg.calendar.api.schedule.publicschedule.service;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.oauth2.sdk.util.StringUtils;

import gg.auth.UserDto;
import gg.calendar.api.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.calendar.api.schedule.publicschedule.controller.request.PublicScheduleUpdateReqDto;
import gg.calendar.api.schedule.publicschedule.controller.response.PublicScheduleUpdateResDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
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

		validateScheduleUpdate(publicScheduleUpdateReqDto);
		publicSchedule.update(publicScheduleUpdateReqDto.getClassification(), publicScheduleUpdateReqDto.getTags(),
			publicScheduleUpdateReqDto.getTitle(), publicScheduleUpdateReqDto.getContent(),
			publicScheduleUpdateReqDto.getLink(), publicScheduleUpdateReqDto.getStartTime(),
			publicScheduleUpdateReqDto.getEndTime(), publicScheduleUpdateReqDto.isAlarm(),
			publicScheduleUpdateReqDto.getColor());
		return PublicScheduleUpdateResDto.from(publicSchedule);
	}

	private void validateScheduleUpdate(PublicScheduleUpdateReqDto publicScheduleUpdateReqDto)
	{
		if (publicScheduleUpdateReqDto.getStartTime().isAfter(publicScheduleUpdateReqDto.getEndTime()))
		{
			throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
		}
		if (publicScheduleUpdateReqDto.getEndTime().isBefore(publicScheduleUpdateReqDto.getStartTime()))
		{
			throw new IllegalArgumentException("종료시간이 시작시간보다 빠를 수 없습니다.");
		}
		if (publicScheduleUpdateReqDto.getEndTime().isEqual(publicScheduleUpdateReqDto.getStartTime()))
		{
			throw new IllegalArgumentException("시작 시간과 종료시간이 같을 수 없습니다.");
		}
		if (StringUtils.isBlank(publicScheduleUpdateReqDto.getTitle())) {
			throw new IllegalArgumentException("일정 제목은 필수입니다.");
		}
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
