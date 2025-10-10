package org.growith.be.growith.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.dto.request.AuthRequestDTO;
import org.growith.be.growith.domain.auth.service.command.AuthCommandService;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
@Tag(name = "인증 API")
public class AuthController {

    private final AuthCommandService authCommandService;

    @Operation(summary = "회원가입 API", description = "최초 회원가입 시 필요한 정보를 포함하여 회원가입 진행")
    @PostMapping("/sign-up")
    public ApiResponse<String> signUp(@RequestBody @Valid AuthRequestDTO.SignUp request){
        authCommandService.signUp(request);
        return ApiResponse.onSuccess(null);
    }
}
