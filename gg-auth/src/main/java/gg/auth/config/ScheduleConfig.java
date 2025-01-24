package gg.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class ScheduleConfig {
	@Value("${spring.security.oauth2.client.registration.42.client-id}")
	private String clientId;
	@Value("${spring.security.oauth2.client.registration.42.client-secret}")
	private String clientSecret;
	@Value("${spring.security.oauth2.client.provider.42.token-uri}")
	private String tokenUri;
}
