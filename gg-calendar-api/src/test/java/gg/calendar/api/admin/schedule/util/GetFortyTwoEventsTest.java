package gg.calendar.api.admin.schedule.util;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gg.data.user.User;
import gg.utils.TestDataUtils;
import gg.utils.annotation.IntegrationTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@IntegrationTest
@Transactional
@AutoConfigureMockMvc
public class GetFortyTwoEventsTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestDataUtils testDataUtils;

	private User user;

	private String accessToken;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		user = testDataUtils.createAdminUser();
		accessToken = testDataUtils.getLoginAccessTokenFromUser(user);
	}

	@Test
	@DisplayName("성공")
	public void testGetFortyTwoEvents() throws Exception {
		// given
		// when
		// then
	}
}
