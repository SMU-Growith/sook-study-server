package org.growith.be.growith.global.annotation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.error.code.status.AuthErrorCode;
import org.growith.be.growith.global.error.exception.handler.AuthException;
import org.growith.be.growith.global.security.domain.CustomUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticatedMemberResolver implements HandlerMethodArgumentResolver {

    private final SecurityContextRepository securityContextRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUser.class)
                && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("어노테이션에 들어옴");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.info("리턴값있음 -1");
            throw new AuthException(AuthErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails customUserDetails) {
            log.info("리턴값있음");
            return customUserDetails.getUser();
        }
        log.info("리턴값없음 -2");
        throw new AuthException(AuthErrorCode.UNAUTHORIZED);
    }
}
