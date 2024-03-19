package gg.utils;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import gg.data.recruit.application.Application;
import gg.data.recruit.application.RecruitStatus;
import gg.data.recruit.recruitment.Recruitments;
import gg.data.user.User;
import gg.repo.recruit.user.application.ApplicationRepository;
import gg.repo.recruit.user.application.RecruitStatusRepository;
import gg.repo.recruit.user.recruitment.RecruitmentRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecruitMockData {
	private final RecruitmentRepository recruitmentRepository;
	private final ApplicationRepository applicationRepository;
	private final RecruitStatusRepository recruitStatusRepository;

	public Recruitments createRecruitments() {
		Recruitments recruitments = new Recruitments("title", "contents", "generation",
			LocalDateTime.now(), LocalDateTime.now().plusDays(1));
		return recruitmentRepository.save(recruitments);
	}

	public Recruitments createRecruitmentsDel() {
		Recruitments recruitments = new Recruitments("title", "contents", "generation",
			LocalDateTime.now(), LocalDateTime.now().plusDays(1));
		recruitments.del();
		return recruitmentRepository.save(recruitments);
	}

	public Application createApplication(User user, Recruitments recruitments) {
		Application application = new Application(user, recruitments);
		return applicationRepository.save(application);
	}

	public RecruitStatus createRecruitStatus(Application application) {
		RecruitStatus recruitStatus = new RecruitStatus(application);
		return recruitStatusRepository.save(recruitStatus);
	}
}
