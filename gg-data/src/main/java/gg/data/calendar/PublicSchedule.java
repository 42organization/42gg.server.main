package gg.data.calendar;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import gg.data.BaseTimeEntity;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.ScheduleStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicSchedule extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20, columnDefinition = "VARCHAR(10)")
	private DetailClassification classification;

	@OneToMany(mappedBy = "publicSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Tag> tags = new HashSet<>();

	@Column(nullable = false)
	private String author;

	@Column(nullable = false)
	private String title;

	private String content;

	private String link;

	@Column(nullable = false)
	private LocalDateTime startTime;

	@Column(nullable = false)
	private LocalDateTime endTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "VARCHAR(10)")
	private ScheduleStatus status;

	@Builder
	public PublicSchedule(DetailClassification classification, Set<Tag> tags, String author, String title,
		String content, String link, LocalDateTime startTime, LocalDateTime endTime) {
		this.classification = classification;
		this.tags = tags;
		this.author = author;
		this.title = title;
		this.content = content;
		this.link = link;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = ScheduleStatus.ACTIVATE;
	}

	// public void update(DetailClassification classification, Set<Tag> tags, String author, String title, String content,
	// 	String link, LocalDateTime startTime, LocalDateTime endTime) {
	//
	// }
	//

	public void update(DetailClassification classification, Set<Tag> tags, String title, String content, String link,
		LocalDateTime startTime, LocalDateTime endTime) {

		this.classification = classification;
		this.tags = tags;
		this.title = title;
		this.content = content;
		this.link = link;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void delete()
	{
		this.status = ScheduleStatus.DELETE;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
}
