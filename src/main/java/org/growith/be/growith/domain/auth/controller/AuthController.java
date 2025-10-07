package org.growith.be.growith.domain.auth.controller;

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
public class AuthController {

    private final AuthCommandService authCommandService;


    @PostMapping("/sign-up")
    public ApiResponse<String> signUp(@RequestBody AuthRequestDTO.SignUp request){
        authCommandService.signUp(request);
        return ApiResponse.onSuccess(null);
    }


}
