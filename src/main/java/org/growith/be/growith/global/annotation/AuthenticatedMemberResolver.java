package org.growith.be.growith.global.annotation;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.global.security.domain.CustomUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
        try {
            UserDetails userDetails = (UserDetails) securityContextRepository.loadDeferredContext(webRequest.getNativeRequest(HttpServletRequest.class)).get().getAuthentication().getPrincipal();
            if (userDetails instanceof CustomUserDetails customUserDetails) {
                return customUserDetails.getUser();
            }
            return null;
        } catch (Exception e){
            return null;
        }
    }
}
