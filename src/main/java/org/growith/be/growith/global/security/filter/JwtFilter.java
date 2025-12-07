package org.growith.be.growith.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growith.be.growith.domain.auth.service.query.TokenStorageQueryService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.service.query.UserQueryService;
import org.growith.be.growith.global.error.code.status.GeneralErrorCode;
import org.growith.be.growith.global.error.exception.GeneralException;
import org.growith.be.growith.global.security.constants.AuthenticationConstants;
import org.growith.be.growith.global.security.domain.CustomUserDetails;
import org.growith.be.growith.global.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenStorageQueryService tokenStorageQueryService;
    private final UserQueryService userQueryService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 있을 때만 검증 시도
        if (jwtUtil.validateToken(token) && !tokenStorageQueryService.isBlackList(token)) {
            try {
                Long userId = jwtUtil.getUserId(token);
                User user = userQueryService.findById(userId);
                CustomUserDetails customUserDetails = new CustomUserDetails(user);

                Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(customUserDetails, "", customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (GeneralException e) {
                throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthenticationConstants.AUTH_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(AuthenticationConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(AuthenticationConstants.TOKEN_PREFIX.length());
        }
        return null;
    }
}
