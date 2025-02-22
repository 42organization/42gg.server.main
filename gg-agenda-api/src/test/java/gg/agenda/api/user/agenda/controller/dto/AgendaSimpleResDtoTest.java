package gg.agenda.api.user.agenda.controller.dto;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import gg.agenda.api.user.agenda.controller.response.AgendaSimpleResDto;
import gg.data.agenda.Agenda;
import gg.utils.annotation.UnitTest;

@UnitTest
class AgendaSimpleResDtoTest {

	@Nested
	@DisplayName("AgendaSimpleResponseDto 생성")
	class CreateAgendaSimpleResDto {

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		@DisplayName("AgendaSimpleResponseDto 생성 성공")
		void createAgendaSimpleResponseDtoSuccess(boolean value) {
			// when
			Agenda agenda = Agenda.builder()
				.isOfficial(value)
				.build();

			// given
			AgendaSimpleResDto dto = AgendaSimpleResDto.MapStruct.INSTANCE.toDto(agenda);

			// then
			assertThat(dto).isNotNull();
			assertThat(dto.getAgendaKey()).isEqualTo(agenda.getAgendaKey());
			assertThat(dto.getIsOfficial()).isEqualTo(value);
		}
	}
}
