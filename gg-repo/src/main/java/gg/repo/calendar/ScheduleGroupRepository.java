package gg.repo.calendar;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.data.calendar.ScheduleGroup;

public interface ScheduleGroupRepository extends JpaRepository<ScheduleGroup, Long> {
}
