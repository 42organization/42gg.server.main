package gg.data.calendar;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import gg.data.BaseTimeEntity;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateSchedule extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "public_schedule_id", nullable = false)
	private PublicSchedule publicSchedule;

	@Column(columnDefinition = "boolean default true")
	private boolean alarm;

	private String color;

	@Column(nullable = false)
	private ScheduleStatus status;
  
	@Builder
	public PrivateSchedule(User user, PublicSchedule publicSchedule, String color) {
		this.user = user;
		this.publicSchedule = publicSchedule;
		this.color = color;
		this.alarm = true;
		this.status = ScheduleStatus.ACTIVATE;
	}

	public static PrivateSchedule of(User user, PublicSchedule publicSchedule, String color) {
		return PrivateSchedule.builder().user(user).publicSchedule(publicSchedule).color(color).build();
	}

	public void update(DetailClassification classification, Set<Tag> tags, String title, String content, String link,
		LocalDateTime startTime, LocalDateTime endTime, boolean alarm, String color) {
  }
  
	public void update(DetailClassification classification, Set<Tag> tags,
		String title, String content, String link, LocalDateTime startTime, LocalDateTime endTime,
		boolean alarm, String color) {

		this.publicSchedule.update(classification, tags, title, content, link, startTime, endTime);
		this.alarm = alarm;
		this.color = color;

	}
}
