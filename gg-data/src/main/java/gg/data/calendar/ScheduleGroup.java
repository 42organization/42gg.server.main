package gg.data.calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import gg.data.BaseTimeEntity;
import gg.data.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleGroup extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotNull
	@Column(nullable = false)
	private String title;

	@NotNull
	@Column(nullable = false)
	private String backgroundColor;

	@Builder
	private ScheduleGroup(User user, String title, String backgroundColor) {
		this.user = user;
		this.title = title;
		this.backgroundColor = backgroundColor;
	}

	public void update(String title, String backgroundColor) {
		this.title = title;
		this.backgroundColor = backgroundColor;
	}
}
