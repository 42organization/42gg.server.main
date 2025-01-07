package gg.repo.calendar;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.data.calendar.ScheduleGroup;

public interface ScheduleGroupRepository extends JpaRepository<ScheduleGroup, Long> {
	List<ScheduleGroup> findByUserId(Long userId);
}
