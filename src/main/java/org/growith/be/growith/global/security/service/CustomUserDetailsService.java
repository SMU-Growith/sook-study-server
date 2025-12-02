package org.growith.be.growith.global.security.service;


import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.AuthErrorCode;
import org.growith.be.growith.global.error.exception.handler.AuthException;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.growith.be.growith.global.security.domain.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(email)
                .orElseThrow( ()-> new AuthException(AuthErrorCode.NOT_FOUND_LOGIN_MEMBER));
        if (user.getPassword() == null){
            throw new AuthException(AuthErrorCode.FAIL_AUTH_LOGIN);
        }
        return new CustomUserDetails(user);
    }
}
