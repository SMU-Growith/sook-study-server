package org.growith.be.growith.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.dto.request.AuthRequestDTO;
import org.growith.be.growith.domain.auth.dto.response.AuthResponseDTO;
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
    public ApiResponse<AuthResponseDTO.TokenResult> signUp(@RequestBody @Valid AuthRequestDTO.SignUp request){
        AuthResponseDTO.TokenResult tokenResult = authCommandService.signUp(request);
        return ApiResponse.onSuccess(tokenResult);
    }

    @Operation(summary = "로그인 API", description = "로그인 API")
    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO.TokenResult> login(@RequestBody @Valid AuthRequestDTO.Login request){
        AuthResponseDTO.TokenResult login = authCommandService.login(request);
        return ApiResponse.onSuccess(login);
    }

    @Operation(summary = "Access Token 재발급 API", description = "토큰 재발급 API")
    @PostMapping("/reissue")
    public ApiResponse<String> reissueToken(HttpServletRequest request, HttpServletResponse response){
//        authCommandService.reissueToken(request, response);
        return ApiResponse.onSuccess(null);
    }


    @Operation(summary = "로그아웃 API", description = "로그아웃 API")
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request, HttpServletResponse response){
        // TODO: service 코드 구체화 필요
        return ApiResponse.onSuccess(null);
    }



}
