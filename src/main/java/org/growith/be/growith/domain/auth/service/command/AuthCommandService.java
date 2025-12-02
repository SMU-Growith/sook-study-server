package org.growith.be.growith.domain.auth.service.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.growith.be.growith.domain.auth.dto.request.AuthRequestDTO;
import org.growith.be.growith.domain.auth.dto.response.AuthResponseDTO;

public interface AuthCommandService {
    AuthResponseDTO.TokenResult signUp(AuthRequestDTO.SignUp request);
    AuthResponseDTO.TokenResult login(AuthRequestDTO.Login request);
//    void reissueToken(HttpServletRequest request, HttpServletResponse response);
}
