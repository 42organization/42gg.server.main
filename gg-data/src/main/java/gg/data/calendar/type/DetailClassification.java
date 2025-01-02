package gg.data.calendar.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DetailClassification {
	EVENT("42행사") {
		@Override
		public boolean isValid(EventTag eventTag, JobTag jobTag, TechTag techTag) {
			return eventTag != null && jobTag == null && techTag == null;
		}
	},

	JOB_NOTICE("취업공고") {
		@Override
		public boolean isValid(EventTag eventTag, JobTag jobTag, TechTag techTag) {
			return eventTag == null && jobTag != null && techTag != null;
		}
	},

	PRIVATE_SCHEDULE("개인일정") {
		@Override
		public boolean isValid(EventTag eventTag, JobTag jobTag, TechTag techTag) {
			return false;
		}
	};

	private final String value;

	public abstract boolean isValid(EventTag eventTag, JobTag jobTag, TechTag techTag);
}
