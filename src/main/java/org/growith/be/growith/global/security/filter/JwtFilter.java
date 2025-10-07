package org.growith.be.growith.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.service.query.TokenStorageQueryService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.service.query.UserQueryService;
import org.growith.be.growith.global.security.constants.AuthenticationConstants;
import org.growith.be.growith.global.security.domain.CustomUserDetails;
import org.growith.be.growith.global.util.CookieUtil;
import org.growith.be.growith.global.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserQueryService userQueryService;
    private final TokenStorageQueryService tokenStorageQueryService;
//    private final SecurityContextRepository securityContextRepository;
//    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if (token != null && !tokenStorageQueryService.isBlackList(token)) {
            try {
                Long userId = jwtUtil.getUserId(token);
                User user = userQueryService.findById(userId);
                CustomUserDetails customUserDetails = new CustomUserDetails(user);

                Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(customUserDetails, "", customUserDetails.getAuthorities());
//                this.successfulAuthentication(request, response, authentication);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
//                handleException(response, e);
            }
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    private String getToken(HttpServletRequest request) {
        return CookieUtil.getCookie(request, AuthenticationConstants.ACCESS_TOKEN_NAME);
    }
}
