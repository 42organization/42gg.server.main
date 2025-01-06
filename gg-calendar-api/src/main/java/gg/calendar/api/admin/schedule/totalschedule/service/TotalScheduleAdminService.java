package gg.calendar.api.admin.schedule.totalschedule.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.schedule.totalschedule.controller.response.TotalScheduleAdminResDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.utils.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TotalScheduleAdminService {

	private final PublicScheduleAdminRepository publicScheduleAdminRepository;

	public PageResponseDto<TotalScheduleAdminResDto> findAllByClassification(DetailClassification detailClassification,
		int page, int size) {

		Pageable pageable = PageRequest.of(page - 1, size,
			Sort.by(Sort.Order.asc("status"), Sort.Order.asc("startTime")));

		Page<PublicSchedule> publicSchedules = publicScheduleAdminRepository.findAllByClassification(
			detailClassification, pageable);

		List<TotalScheduleAdminResDto> publicScheduleList = publicSchedules.stream()
			.map(TotalScheduleAdminResDto::new)
			.collect(Collectors.toList());
		return PageResponseDto.of(publicSchedules.getTotalElements(), publicScheduleList);
	}

	public PageResponseDto<TotalScheduleAdminResDto> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size,
			Sort.by(Sort.Order.asc("status"), Sort.Order.asc("startTime")));

		Page<PublicSchedule> publicSchedules = publicScheduleAdminRepository.findAll(pageable);

		List<TotalScheduleAdminResDto> publicScheduleList = publicSchedules.stream()
			.map(TotalScheduleAdminResDto::new)
			.collect(Collectors.toList());
		return PageResponseDto.of(publicSchedules.getTotalElements(), publicScheduleList);
	}
}
