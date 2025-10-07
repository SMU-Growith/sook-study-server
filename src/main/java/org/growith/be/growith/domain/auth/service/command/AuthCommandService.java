package org.growith.be.growith.domain.auth.service.command;

import org.growith.be.growith.domain.auth.dto.request.AuthRequestDTO;

public interface AuthCommandService {
    public void signUp(AuthRequestDTO.SignUp request);
}
