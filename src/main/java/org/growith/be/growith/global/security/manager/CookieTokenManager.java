package org.growith.be.growith.global.security.manager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.dto.response.AuthResponseDTO;
import org.growith.be.growith.domain.auth.service.command.TokenCommandService;
import org.growith.be.growith.domain.auth.service.command.TokenStorageCommandService;
import org.growith.be.growith.domain.auth.service.query.TokenQueryService;
import org.growith.be.growith.global.security.constants.AuthenticationConstants;
import org.growith.be.growith.global.security.domain.CustomUserDetails;
import org.growith.be.growith.global.util.CookieUtil;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieTokenManager implements TokenManager{

    private final TokenStorageCommandService tokenStorageCommandService;
    private final TokenCommandService tokenCommandService;
    private final TokenQueryService tokenQueryService;

    @Override
    public void addToken(HttpServletRequest request, HttpServletResponse response, CustomUserDetails customUserDetails) {
        AuthResponseDTO.Login loginResponse = tokenCommandService.createLoginToken(customUserDetails);
        for (String token : new String[] {CookieUtil.getCookie(request, AuthenticationConstants.ACCESS_TOKEN_NAME), CookieUtil.getCookie(request, AuthenticationConstants.REFRESH_TOKEN_NAME)}) {
            if (token != null){
                tokenStorageCommandService.addBlackList(token);
            }
        }

        CookieUtil.addCookie(request, response, AuthenticationConstants.ACCESS_TOKEN_NAME, loginResponse.accessToken(), (int) tokenQueryService.getAccessTokenExpiration().toSeconds());
        CookieUtil.addCookie(request, response, AuthenticationConstants.REFRESH_TOKEN_NAME, loginResponse.refreshToken(), (int) tokenQueryService.getRefreshTokenExpiration().toSeconds());
        tokenStorageCommandService.addRefreshToken(customUserDetails.getId(), loginResponse.refreshToken());
    }
}
