package gg.admin.repo.calendar;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.data.calendar.ScheduleGroup;

@Repository
public interface ScheduleGroupAdminRepository extends JpaRepository<ScheduleGroup, Long> {

	Optional<ScheduleGroup> findByScheduleGroupId(Long scheduleGroupId);
}
