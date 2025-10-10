package org.growith.be.growith.domain.auth.converter;

import org.growith.be.growith.domain.auth.dto.request.AuthRequestDTO;
import org.growith.be.growith.domain.auth.dto.response.AuthResponseDTO;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;
import org.growith.be.growith.domain.user.entity.enums.UserRole;

public class AuthConverter {

    public static AuthResponseDTO.Login toLoginResponse(String accessToken, String refreshToken) {
        return AuthResponseDTO.Login.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static User toLocalUser(AuthRequestDTO.SignUp request){

        return User.builder()
                .loginId(request.loginId())
                .email(request.email())
                .password(request.password())
                .nickName(request.nickname())
                .major(request.major())
                .studentStatus(request.studentStatus())
                .userRole(UserRole.USER)
                .build();
    }
}
