package gg.recruit.api.admin.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.admin.repo.recruit.ApplicationAdminRepository;
import gg.admin.repo.recruit.RecruitmentAdminRepository;
import gg.data.recruit.application.Application;
import gg.data.recruit.application.enums.ApplicationStatus;
import gg.data.recruit.recruitment.CheckList;
import gg.data.recruit.recruitment.Question;
import gg.data.recruit.recruitment.Recruitment;
import gg.data.recruit.recruitment.enums.InputType;
import gg.recruit.api.admin.service.dto.Form;
import gg.recruit.api.admin.service.dto.UpdateApplicationStatusDto;
import gg.recruit.api.admin.service.dto.UpdateRecruitStatusParam;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.BusinessException;
import gg.utils.exception.custom.NotExistException;
import gg.utils.exception.recruitment.InvalidCheckListException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentAdminService {
	private final RecruitmentAdminRepository recruitmentAdminRepository;
	private final ApplicationAdminRepository applicationAdminRepository;

	/**
	 * 채용 공고를 생성한다.
	 * Recruitment, Question, CheckList을 저장한다.
	 * @param recruitment Recruitment
	 * @param forms Question, CheckList이 포함
	 * @return Recruitment 생성된 채용 공고
	 */
	@Transactional
	public Recruitment createRecruitment(Recruitment recruitment, List<Form> forms) {
		for (int i = 0; i < forms.size(); i++) {
			Form form = forms.get(i);
			Question question = form.toQuestion(recruitment, i + 1);
			InputType inputType = question.getInputType();

			if (inputType == InputType.SINGLE_CHECK || inputType == InputType.MULTI_CHECK) {
				addCheckList(question, form.getCheckList());
			}
		}
		return recruitmentAdminRepository.save(recruitment);
	}

	@Transactional
	public void updateRecruitStatus(UpdateRecruitStatusParam updateRecruitStatusParam) {
		Recruitment recruitments = recruitmentAdminRepository.findById(updateRecruitStatusParam.getRecruitId())
			.orElseThrow(() -> new NotExistException("Recruitment not found."));
		recruitments.setFinish(updateRecruitStatusParam.getFinish());
	}

	/**
	 * 공고 종료 날짜 기준으로 내림차순(최근순) 정렬하여 채용 공고를 조회한다.
	 * @param pageable
	 * @return 조회된 채용 공고 리스트
	 */
	@Transactional(readOnly = true)
	public List<Recruitment> getAllRecruitments(Pageable pageable) {
		Slice<Recruitment> allByOrderByEndDateDesc = recruitmentAdminRepository.findAllByOrderByEndTimeDesc(pageable);
		return allByOrderByEndDateDesc.getContent();
	}

	/**
	 * @param question 질문
	 * @param checkList 선택지
	 * @throws InvalidCheckListException 선택지가 필요한데 비어있을 때 발생
	 */
	private void addCheckList(Question question, List<String> checkList) {
		if (checkList == null || checkList.isEmpty()) {
			throw new InvalidCheckListException();
		}
		for (String content : checkList) {
			new CheckList(question, content);
		}
	}

	/**
	 * 최종 결과 등록 후 알림
	 * @param dto
	 */
	@Transactional
	public void updateFinalApplicationStatusAndNotification(UpdateApplicationStatusDto dto) {
		finalApplicationStatusCheck(dto.getStatus());
		updateApplicationStatus(dto);
		// 사이클로 SNS Noti 접근이 불가능해 알림 기능 추후 구현
	}

	/**
	 * 최종 결과에 해당하는 값인지 검증
	 * @param status
	 */
	private void finalApplicationStatusCheck(ApplicationStatus status) {
		if (!status.isFinal) {
			throw new BusinessException(ErrorCode.BAD_ARGU);
		}
	}

	/**
	 * 지원서 상태 변경
	 * @param dto
	 */
	public void updateApplicationStatus(UpdateApplicationStatusDto dto) {
		Application application = applicationAdminRepository
			.findByIdAndRecruitId(dto.getApplicationId(), dto.getRecruitId())
			.orElseThrow(NullPointerException::new);
		application.updateApplicationStatus(dto.getStatus());
	}
}
