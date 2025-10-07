package org.growith.be.growith.global.security.service;


import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
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
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getPassword() == null){
            // TODO: Exception 던지는 코드 작성
        }
        return new CustomUserDetails(user);
    }
}
