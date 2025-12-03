package org.growith.be.growith.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

public record AuthRequestDTO() {

    public record Login(
            @NotBlank(message = "로그인 아이디는 필수입니다")
            String loginId,
            @NotBlank(message = "비밀번호는 필수입니다")
            String password
    ){}

    public record SignUp(
            @NotBlank(message = "로그인 아이디는 필수입니다")
            String loginId,
            @Email @NotBlank
            String email,
            @NotBlank(message = "비밀번호는 필수입니다")
            String password,
            String nickname,
            Major major,
            StudentStatus studentStatus,
            String phoneNumber
    ){}



}
