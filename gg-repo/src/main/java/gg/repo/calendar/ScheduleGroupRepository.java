package gg.repo.calendar;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.data.calendar.ScheduleGroup;

public interface ScheduleGroupRepository extends JpaRepository<ScheduleGroup, Long> {
	Optional<ScheduleGroup> findByIdAndUserId(Long groupId, Long userId);

	List<ScheduleGroup> findByUserId(Long userId);
}
