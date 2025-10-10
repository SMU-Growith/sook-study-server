package org.growith.be.growith.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

public record AuthRequestDTO() {

    public record Login(
            @Email
            String email,
            String password
    ){}

    public record SignUp(
            String loginId,
            @Email
            String email,
            String password,
            String nickname,
            Major major,
            StudentStatus studentStatus,
            String phoneNumber
    ){}



}
