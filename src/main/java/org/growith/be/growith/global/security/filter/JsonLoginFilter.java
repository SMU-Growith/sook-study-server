package org.growith.be.growith.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.dto.request.AuthRequestDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Getter
@RequiredArgsConstructor
public class JsonLoginFilter extends OncePerRequestFilter {

    private static final RequestMatcher DEFAULT_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/auth/login");
    private final AuthenticationManager authenticationManager;
//    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requiresAuthentication(request)) {
            try {
                    Authentication authentication = attemptAuthentication(request, response);

                    if (authentication == null) {
                        return;
                    }
                } catch (Exception e) {
                    // TODO: Exceptionhandling 필요
                }
            }  else {
                filterChain.doFilter(request, response);
            }
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthRequestDTO.Login requestBody = getBodyInRequest(request);
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(requestBody.email(), requestBody.password());
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e){
            // TODO: Exceptionhandling 필요
        }
        return null;
    }

    private AuthRequestDTO.Login getBodyInRequest(HttpServletRequest request) throws IOException{
        String content = new String((new HttpServletRequestWrapper(request)).getInputStream().readAllBytes());
        ObjectMapper om = new ObjectMapper();
        return om.readValue(content, AuthRequestDTO.Login.class);
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        String contentType = request.getContentType();
        return DEFAULT_REQUEST_MATCHER.matches(request) && contentType != null && contentType.equals(MediaType.APPLICATION_JSON_VALUE);
    }

}
