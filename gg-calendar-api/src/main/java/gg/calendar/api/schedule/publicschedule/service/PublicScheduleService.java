package gg.calendar.api.schedule.publicschedule.service;



import static gg.utils.exception.ErrorCode.*;
import java.time.LocalDateTime;
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
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.BusinessException;
import gg.utils.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicScheduleService {
	private final PublicScheduleRepository publicScheduleRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createPublicSchedule(UserDto userDto, PublicScheduleCreateReqDto publicScheduleCreateReqDto) {

		validateScheduleTime(publicScheduleCreateReqDto.getStartTime(),publicScheduleCreateReqDto.getEndTime());
		// Todo: 값이 누락된 경우 생각해봐야하나? 어차피 table에서 걸러주는데? 그래도 서비스단에서 먼저 확인하는 것이 맞겠지...?
		PublicSchedule publicSchedule = PublicScheduleCreateReqDto.of(userDto.getIntraId(),
			publicScheduleCreateReqDto);
		publicScheduleRepository.save(publicSchedule);
	}

	private void validateScheduleTime(LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime))
		{
			throw new BusinessException(CALENDAR_AFTER_DATE);
		}
		if (startTime.isBefore(endTime))
		{
			throw new BusinessException(CALENDAR_BEFORE_DATE);
		}
		if (endTime.isEqual(startTime))
		{
			throw new BusinessException(CALENDAR_EQUAL_DATE);
		}
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
		PublicSchedule publicSchedule = publicScheduleRepository.findByUserIdAndSchduleId(userId, scheduleId).orElseThrow(()-> new IllegalArgumentException("해당 일정이 없습니다."));
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
		PublicSchedule publicSchedule = publicScheduleRepository.findByUserIdAndSchduleId(userId, scheduleId).orElseThrow(()-> new IllegalArgumentException("해당 일정이 없습니다"));
		PublicSchedule publicSchedule = publicSchduleRepository.findByUserIdAndSchduleId(userId, scheduleId).orElseThrow(()-> new IllegalArgumentException("해당 일정이 없습니다"));
		if (publicSchedule.getStatus() == ScheduleStatus.DELETE) {
			throw new IllegalArgumentException("이미 삭제된 일정입니다.");
		}
		publicSchedule.delete();
	}
}
