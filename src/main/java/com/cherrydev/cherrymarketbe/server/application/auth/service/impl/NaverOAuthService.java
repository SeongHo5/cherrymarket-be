package com.cherrydev.cherrymarketbe.server.application.auth.service.impl;

import com.cherrydev.cherrymarketbe.server.application.auth.service.NaverUserFeignClient;
import com.cherrydev.cherrymarketbe.server.application.common.service.RedisService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.SignInResponse;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthAccountInfo;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.request.OAuthRequestDto;
import com.cherrydev.cherrymarketbe.server.domain.auth.dto.response.oauth.OAuthTokenResponse;
import com.cherrydev.cherrymarketbe.server.application.auth.service.NaverAuthFeignClient;
import com.cherrydev.cherrymarketbe.server.application.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.server.application.common.constant.AuthConstant.BEARER_PREFIX;
import static com.cherrydev.cherrymarketbe.server.application.common.constant.AuthConstant.GRANT_TYPE_AUTHORIZATION;
import static com.cherrydev.cherrymarketbe.server.domain.account.enums.RegisterType.NAVER;
import static com.cherrydev.cherrymarketbe.server.application.common.utils.HttpEntityUtils.createHttpEntity;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NaverOAuthService implements OAuthService {

    @Value("${oauth.naver.clientId}")
    private String naverClientId;

    @Value("${oauth.naver.clientSecret}")
    private String naverClientSecret;

    private final NaverAuthFeignClient naverAuthFeignClient;
    private final NaverUserFeignClient naverUserFeignClient;
    private final CommonOAuthService commonOAuthService;
    private final RedisService redisService;
    @Override
    @Transactional
    public ResponseEntity<SignInResponse> signIn(final OAuthRequestDto oAuthRequestDto) {
        String authCode = oAuthRequestDto.getAuthCode();
        String state = oAuthRequestDto.getState();

        OAuthTokenResponse tokenResponse = naverAuthFeignClient.getOAuthToken(
                GRANT_TYPE_AUTHORIZATION, naverClientId, naverClientSecret, authCode, state
        );

        String accessToken = commonOAuthService.getAcessTokenIfExist(tokenResponse);
        OAuthAccountInfo accountInfo = naverUserFeignClient.getAccountInfo(BEARER_PREFIX + accessToken);

        redisService.saveNaverTokenToRedis(tokenResponse, accountInfo.getEmail());
        return commonOAuthService.processSignIn(accountInfo, NAVER.name());
    }

    @Override
    public ResponseEntity<Void> signOut(AccountDetails accountDetails) {
        redisService.deleteNaverTokenFromRedis(accountDetails.getUsername());
        return ResponseEntity.ok().build();
    }

}
