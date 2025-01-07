package gg.calendar.api.admin.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import gg.data.calendar.PublicSchedule;

public class TotalScheduleAdminSpecification {

	public static Specification<PublicSchedule> searchByField(String content, LocalDate startTime, LocalDate endTime,
		String field) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// LocalDate를 LocalDateTime으로 변환
			LocalDateTime startDateTime = startTime.atStartOfDay(); // 00:00:00
			LocalDateTime endDateTime = endTime.atTime(LocalTime.MAX); // 23:59:59

			// 필수 조건
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startDateTime));
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), endDateTime));

			// 동적으로 필드 비교
			if (content != null && field != null) {
				if ("classification".equals(field)) {
					// Enum 필드 처리
					Expression<String> enumAsString = root.get(field).as(String.class);
					predicates.add(
						criteriaBuilder.like(criteriaBuilder.lower(enumAsString), "%" + content.toLowerCase() + "%")
					);
				} else {
					// 일반 String 필드 처리
					Path<String> dynamicField = root.get(field);
					predicates.add(
						criteriaBuilder.like(criteriaBuilder.lower(dynamicField), "%" + content.toLowerCase() + "%")
					);
				}
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
