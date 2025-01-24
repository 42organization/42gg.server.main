// package gg.calendar.api.user.utils.service;
//
// import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.mockito.Mockito;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.web.client.RestTemplateBuilder;
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.github.tomakehurst.wiremock.WireMockServer;
//
// import gg.auth.FortyTwoAuthUtil;
// import gg.auth.config.ScheduleConfig;
// import gg.utils.annotation.IntegrationTest;
// import gg.utils.external.ApiUtil;
//
// @IntegrationTest
// @AutoConfigureMockMvc
// class FortyTwoEventApiClientTest {
// 	private WireMockServer mockServer;
// 	private FortyTwoEventApiClient apiClient;
//
// 	@BeforeEach
// 	void setUp() {
// 		mockServer = new WireMockServer(options().port(8089));
// 		mockServer.start();
//
// 		ObjectMapper objectMapper = new ObjectMapper();
// 		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
//
// 		ApiUtil apiUtil = new ApiUtil(objectMapper, restTemplateBuilder);
// 		OAuth2AuthorizedClientService clientService = Mockito.mock(OAuth2AuthorizedClientService.class);
// 		ScheduleConfig scheduleConfig = Mockito.mock(ScheduleConfig.class);
// 		FortyTwoAuthUtil fortyTwoAuthUtil = new FortyTwoAuthUtil(apiUtil, clientService, scheduleConfig);
// 		apiClient = new FortyTwoEventApiClient(apiUtil, fortyTwoAuthUtil);
// 	}
//
// }
