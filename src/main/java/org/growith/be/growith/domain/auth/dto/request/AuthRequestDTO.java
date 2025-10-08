package org.growith.be.growith.domain.auth.dto.request;

import jakarta.validation.constraints.Email;

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
            String major,
            String studentStatus,
            String phoneNumber
    ){}



}
