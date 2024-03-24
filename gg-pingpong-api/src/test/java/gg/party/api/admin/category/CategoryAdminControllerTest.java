package gg.party.api.admin.category;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import gg.auth.utils.AuthTokenProvider;
import gg.data.party.Category;
import gg.data.user.User;
import gg.data.user.type.RacketType;
import gg.data.user.type.RoleType;
import gg.data.user.type.SnsType;
import gg.party.api.admin.category.controller.request.CategoryAddAdminReqDto;
import gg.party.api.admin.report.service.ReportAdminService;
import gg.repo.party.CategoryRepository;
import gg.utils.TestDataUtils;
import gg.utils.annotation.IntegrationTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@IntegrationTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	TestDataUtils testDataUtils;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	AuthTokenProvider tokenProvider;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ReportAdminService reportAdminService;
	User userTester;
	String userAccessToken;
	Category testCategory;

	@Nested
	@DisplayName("카테고리 추가 테스트")
	class CategoryAdd {
		@BeforeEach
		void beforeEach() {
			userTester = testDataUtils.createNewUser("adminTester", "adminTester",
				RacketType.DUAL, SnsType.SLACK, RoleType.ADMIN);
			userAccessToken = tokenProvider.createToken(userTester.getId());
			testCategory = testDataUtils.createNewCategory("test");
		}

		@Test
		@DisplayName("카테고리 추가 성공 201")
		public void Success() throws Exception {
			//given
			String url = "/party/admin/categories";
			CategoryAddAdminReqDto categoryAddAdminReqDto = new CategoryAddAdminReqDto("category");
			String jsonRequest = objectMapper.writeValueAsString(categoryAddAdminReqDto);
			//when
			String contentAsString = mockMvc.perform(post(url)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + userAccessToken))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();
			//then
			assertThat(categoryRepository.findAll()).isNotNull();
		}

		@Test
		@DisplayName("이미 존재하는 카테고리로 인한 에러 400")
		public void fail() throws Exception {
			//given
			String url = "/party/admin/categories";
			testDataUtils.createNewCategory("category");
			CategoryAddAdminReqDto categoryAddAdminReqDto = new CategoryAddAdminReqDto("category");
			String jsonRequest = objectMapper.writeValueAsString(categoryAddAdminReqDto);
			//when & then
			String contentAsString = mockMvc.perform(post(url)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + userAccessToken))
				.andExpect(status().isBadRequest()).toString();
		}
	}
}
