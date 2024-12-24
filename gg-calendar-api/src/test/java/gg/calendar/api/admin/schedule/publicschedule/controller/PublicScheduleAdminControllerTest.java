package gg.calendar.api.admin.schedule.publicschedule.controller;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import gg.calendar.api.admin.PublicScheduleMockData;
import gg.data.agenda.Agenda;
import gg.data.calendar.PublicSchedule;
import gg.data.user.User;
import gg.utils.TestDataUtils;
import gg.utils.annotation.IntegrationTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@IntegrationTest
@Transactional
@AutoConfigureMockMvc
public class PublicScheduleAdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	EntityManager em;

	@Autowired
	private PublicScheduleMockData publicScheduleMockData;

	@Autowired
	private TestDataUtils testDataUtils;

	private User user;

	private String accessToken;

	@BeforeEach
	void setUp() {
		user = testDataUtils.createAdminUser();
		accessToken = testDataUtils.getLoginAccessTokenFromUser(user);
	}

	@Test
	@DisplayName("Admin PublicSchedule 등록 테스트")
	// mockMvc.perform()로 테스트 -> 실제 API 호출
	public void createPublicScheduleTest() {
		PublicSchedule publicSchedule = publicScheduleMockData.createPublicSchedule();

	}
}
