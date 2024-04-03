package gg.recruit.api.admin.service;

import static org.mockito.ArgumentMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import gg.admin.repo.recruit.ApplicationAdminRepository;
import gg.admin.repo.recruit.RecruitmentAdminRepository;
import gg.data.recruit.application.Application;
import gg.data.recruit.application.enums.ApplicationStatus;
import gg.recruit.api.admin.service.dto.UpdateApplicationStatusDto;
import gg.utils.annotation.UnitTest;
import gg.utils.exception.custom.NotExistException;

@UnitTest
class RecruitmentAdminServiceTest {
	@Mock
	RecruitmentAdminRepository recruitmentAdminRepository;
	@Mock
	ApplicationAdminRepository applicationAdminRepository;
	@InjectMocks
	RecruitmentAdminService recruitmentAdminService;

	@Nested
	class UpdateFinalApplicationStatusAndNotification {
		@Test
		@DisplayName("최종 결과가 아닌 ApplicationStatus가 들어오면 실패해야 한다")
		void invalidApplicationStatus() {
			//Arrange
			List<ApplicationStatus> nonFinalStatuses = Arrays.stream(ApplicationStatus.values())
				.filter(status -> !status.isFinal)
				.collect(Collectors.toList());
			List<UpdateApplicationStatusDto> invalidDtoList = nonFinalStatuses.stream()
				.map((status) -> new UpdateApplicationStatusDto(status, 1L, 1L))
				.collect(Collectors.toList());

			//Act
			//Assert
			for (UpdateApplicationStatusDto invalidDto : invalidDtoList) {
				Assertions.assertThatThrownBy(
						() -> recruitmentAdminService.updateFinalApplicationStatusAndNotification(invalidDto))
					.isInstanceOf(NotExistException.class);
			}
		}

		@Test
		@DisplayName("최종 결과인 ApplicationStatus 및 정상적인 dto 들어오면 성공")
		void validApplicationStatus() {
			//Arrange
			List<ApplicationStatus> finalStatuses = Arrays.stream(ApplicationStatus.values())
				.filter(status -> status.isFinal)
				.collect(Collectors.toList());
			List<UpdateApplicationStatusDto> validDtoList = finalStatuses.stream()
				.map((status) -> new UpdateApplicationStatusDto(status, 1L, 1L))
				.collect(Collectors.toList());
			Application application = Mockito.mock(Application.class);

			//Act
			//Assert
			for (UpdateApplicationStatusDto validDto : validDtoList) {
				Mockito.when(applicationAdminRepository.findByIdAndRecruitId(eq(validDto.getApplicationId()),
					eq(validDto.getRecruitId()))).thenReturn(Optional.of(application));
				recruitmentAdminService.updateFinalApplicationStatusAndNotification(validDto);
			}
		}
	}
}
