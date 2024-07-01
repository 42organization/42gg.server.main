package gg.data.agenda;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import gg.data.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
// @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "agenda", uniqueConstraints = {@UniqueConstraint(name = "uk_agenda_key", columnNames = "key")})
public class Agenda extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "key", nullable = false, columnDefinition = "BINARY(16)")
	private byte[] key;

	@Column(name = "title", nullable = false, columnDefinition = "VARCHAR(50)")
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "VARCHAR(500)")
	private String content;

	@Column(name = "deadline", nullable = false, columnDefinition = "DATETIME")
	private String deadline;

	@Column(name = "start_time", nullable = false, columnDefinition = "DATETIME")
	private String startTime;

	@Column(name = "end_time", nullable = false, columnDefinition = "DATETIME")
	private String endTime;

	@Column(name = "min_team", nullable = false, columnDefinition = "INT")
	private int minTeam;

	@Column(name = "max_team", nullable = false, columnDefinition = "INT")
	private int maxTeam;

	@Column(name = "current_team", nullable = false, columnDefinition = "INT")
	private int currentTeam;

	@Column(name = "min_people", nullable = false, columnDefinition = "INT")
	private int minPeople;

	@Column(name = "max_people", nullable = false, columnDefinition = "INT")
	private int maxPeople;

	@Column(name = "poster_uri", columnDefinition = "VARCHAR(255)")
	private String posterUri;

	@Column(name = "host_intra_id", nullable = false, columnDefinition = "VARCHAR(30)")
	private String hostIntraId;

	@Column(name = "location", nullable = false, columnDefinition = "VARCHAR(30)")
	private String location;

	@Column(name = "status", nullable = false, columnDefinition = "VARCHAR(10)")
	private String status;

	@Column(name = "is_official", nullable = false, columnDefinition = "BOOL")
	private boolean isOfficial;

	@Column(name = "is_ranking", nullable = false, columnDefinition = "BOOL")
	private boolean isRanking;

	@Builder
	public Agenda(Long id, byte[] key, String title, String content, String deadline, String startTime, String endTime,
		int minTeam, int maxTeam, int currentTeam, int minPeople, int maxPeople, String posterUri, String hostIntraId,
		String location, String status, boolean isOfficial, boolean isRanking) {
		this.id = id;
		this.key = key;
		this.title = title;
		this.content = content;
		this.deadline = deadline;
		this.startTime = startTime;
		this.endTime = endTime;
		this.minTeam = minTeam;
		this.maxTeam = maxTeam;
		this.currentTeam = currentTeam;
		this.minPeople = minPeople;
		this.maxPeople = maxPeople;
		this.posterUri = posterUri;
		this.hostIntraId = hostIntraId;
		this.location = location;
		this.status = status;
		this.isOfficial = isOfficial;
		this.isRanking = isRanking;
	}
}
