package org.growith.be.growith.domain.auth.dto.request;

import jakarta.validation.constraints.Email;

public record AuthRequestDTO() {

    public record Login(
            @Email
            String email,
            String password
    ){}
}
