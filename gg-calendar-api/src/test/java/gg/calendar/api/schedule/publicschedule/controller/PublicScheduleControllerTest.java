package gg.calendar.api.schedule.publicschedule.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import gg.calendar.api.schedule.publicschedule.service.PublicScheduleService;
import gg.utils.annotation.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import gg.utils.TestDataUtils;
import gg.data.user.User;


@Slf4j
@IntegrationTest
@Transactional
@AutoConfigureMockMvc
public class PublicScheduleControllerTest {

	// @Autowired
	// private ObjectMapper objectMapper;
	//
	// @Autowired
	// private EntityManager em;
	//
	// @Autowired
	// private PublicCalendarMockDate publicCalendarMockDate;
	//
	// // @Autowired
	// // private PublicScheduleRepository publicScheduleRepository;
	//
	// private User user;
	// private String accessToken;
	//
	// @BeforeEach
	// void setUp() {
	// 	user = testDataUtils.createNewUser();
	// 	accessToken = testDataUtils.getLoginAccessTokenFromUser(user);
	// }
	//
	//
	// @Nested
	// @DisplayName("공개 일정 생성")
	// class CreatePublicSchedule {
	//
	// 	@Test
	// 	@DisplayName("올바른 요청으로 공개 일정을 생성한다")
	// 	public void createPublicSchedule_Success() throws  Exception {
	//
	//
	// 	}
	// }
}
