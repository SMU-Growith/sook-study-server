package org.growith.be.growith.domain.auth.converter;

import org.growith.be.growith.domain.auth.dto.request.AuthRequestDTO;
import org.growith.be.growith.domain.auth.dto.response.AuthResponseDTO;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.entity.enums.UserRole;

public class AuthConverter {

    public static AuthResponseDTO.TokenResult toTokenResult(User user, String accessToken, String refreshToken) {
        String personalityTypeName = null;
        if (user.getPersonalityResultType() != null) {
            personalityTypeName = user.getPersonalityResultType().getTypeName();
        }
        
        return AuthResponseDTO.TokenResult.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .major(user.getMajor() != null ? user.getMajor().name() : null)
                .studentStatus(user.getStudentStatus() != null ? user.getStudentStatus().name() : null)
                .phoneNumber(user.getPhoneNumber())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .personalityType(personalityTypeName)
                .build();
    }

    // AuthRequestDTO.SignUp -> User
    public static User toLocalUser(AuthRequestDTO.SignUp request){
        return User.builder()
                .loginId(request.loginId())
                .email(request.email())
                .password(request.password())
                .nickName(request.nickname())
                .major(request.major())
                .studentStatus(request.studentStatus())
                .phoneNumber(request.phoneNumber())
                .userRole(UserRole.USER)
                .build();
    }

    // AuthResponseDTO.LoginResult
    public static AuthResponseDTO.AccessTokenResult toAccessTokenResult(Long userId, String accessToken){
        return AuthResponseDTO.AccessTokenResult.builder()
                .userId(userId)
                .accessToken(accessToken)
                .build();
    }
}
