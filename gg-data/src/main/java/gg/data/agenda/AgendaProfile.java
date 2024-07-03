package gg.data.agenda;

import gg.data.BaseTimeEntity;
import gg.data.agenda.type.Coalition;
import gg.data.agenda.type.Location;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agenda_profile")
public class AgendaProfile extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content", length = 1000, nullable = false)
	private String content;

	@Column(name = "github_url", length = 255, nullable = true)
	private String githubUrl;

	@Column(name = "coalition", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private Coalition coalition;

	@Column(name = "location", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private Location location;

	@Column(name = "user_id", nullable = false, columnDefinition = "BIGINT")
	private Long userId;

	@Builder
	public AgendaProfile(String content, String githubUrl, Coalition coalition, Location location, Long userId) {
		this.content = content;
		this.githubUrl = githubUrl;
		this.coalition = coalition;
		this.location = location;
		this.userId = userId;
	}
}
