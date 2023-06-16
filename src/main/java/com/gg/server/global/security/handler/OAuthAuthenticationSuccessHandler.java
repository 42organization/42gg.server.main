package com.gg.server.global.security.handler;

import com.gg.server.domain.rank.redis.RedisKeyManager;
import com.gg.server.domain.user.User;
import com.gg.server.domain.user.UserRepository;
import com.gg.server.domain.user.exception.UserNotFoundException;
import com.gg.server.domain.user.type.RoleType;
import com.gg.server.global.security.UserPrincipal;
import com.gg.server.global.security.cookie.CookieUtil;
import com.gg.server.global.security.config.properties.AppProperties;
import com.gg.server.global.security.jwt.repository.JwtRedisRepository;
import com.gg.server.global.security.jwt.utils.TokenHeaders;
import com.gg.server.global.security.jwt.utils.AuthTokenProvider;
import com.gg.server.global.utils.ApplicationYmlRead;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtRedisRepository jwtRedisRepository;
    private final UserRepository userRepository;
    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final ApplicationYmlRead applicationYmlRead;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie :cookies) {
            if (cookie.getName().equals(TokenHeaders.ACCESS_TOKEN) ) {
                Long existUserId = tokenProvider.getUserIdFromAccessToken(cookie.getValue());
                CookieUtil.deleteCookie(request, response, TokenHeaders.ACCESS_TOKEN);
                if (existUserId != null) {
                    return deleteKakaoUser(existUserId, response, authentication);
                } else {
                    throw new UserNotFoundException();
                }
            }
        }
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // 쿠키 시간 설정
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        // token 설정
        String accessToken = tokenProvider.createToken(principal.getId());
        String refreshToken = tokenProvider.refreshToken(principal.getId());

        CookieUtil.addCookie(response, TokenHeaders.REFRESH_TOKEN, refreshToken,
                        (int)(refreshTokenExpiry / 1000), applicationYmlRead.getDomain());

        String refTokenKey = RedisKeyManager.getRefKey(principal.getId());
        if (jwtRedisRepository.getRefToken(refTokenKey) != null)
            jwtRedisRepository.deleteRefToken(refTokenKey);
        jwtRedisRepository.addRefToken(refTokenKey, refreshToken, refreshTokenExpiry);
        return UriComponentsBuilder.fromUriString(applicationYmlRead.getFrontUrl())
                .queryParam("token", accessToken)
                .build().toUriString();
    }

    private String deleteKakaoUser(Long existUserId, HttpServletResponse response, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User existUser = userRepository.findById(existUserId).orElseThrow(UserNotFoundException::new);
        User newUser = userRepository.findById(principal.getId()).orElseThrow(UserNotFoundException::new);
        //kakao 계정 사용자가 42 인증
        if (existUser.getRoleType().equals(RoleType.GUEST)) {
            return UriComponentsBuilder.fromUriString(applicationYmlRead.getUserDetailFrontUrl())
                    .queryParam("token", getUserAccessToken(response, newUser, existUser))
                .build().toUriString();
        }
        //기존 user 사용자가 카카오 인증
        if (newUser.getRoleType().equals(RoleType.GUEST)) {
            return UriComponentsBuilder.fromUriString(applicationYmlRead.getUserDetailFrontUrl())
                    .queryParam("token", getUserAccessToken(response, existUser, newUser))
                    .build().toUriString();
        }
        throw new UserNotFoundException();
    }


    private String getUserAccessToken(HttpServletResponse response,
                                        User remainedUser, User deletedUser) {
        remainedUser.updateKakaoId(deletedUser.getKakaoId());
        userRepository.delete(deletedUser);
        // 쿠키 시간 설정
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        // token 설정
        String accessToken = tokenProvider.createToken(remainedUser.getId());
        String refreshToken = tokenProvider.refreshToken(remainedUser.getId());

        CookieUtil.addCookie(response, TokenHeaders.REFRESH_TOKEN, refreshToken,
                (int)(refreshTokenExpiry / 1000), applicationYmlRead.getDomain());
        return accessToken;
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
    }


}


