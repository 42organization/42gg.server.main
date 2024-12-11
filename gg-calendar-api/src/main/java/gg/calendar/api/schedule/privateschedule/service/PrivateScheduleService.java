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
}
