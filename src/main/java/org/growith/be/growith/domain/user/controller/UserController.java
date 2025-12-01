package org.growith.be.growith.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.user.converter.UserConverter;
import org.growith.be.growith.domain.user.dto.request.UserRequestDTO;
import org.growith.be.growith.domain.user.dto.response.UserResponseDTO;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.service.command.UserCommandService;
import org.growith.be.growith.global.annotation.AuthenticatedUser;
import org.growith.be.growith.global.error.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "사용자 정보 API")
public class UserController {

    private final UserCommandService userCommandService;

    @Operation(summary = "사용자 정보 수정 API", description = "마이페이지에서 볼 수 있는 사용자 정보 수정 API")
    @PatchMapping("/profile")
    public ApiResponse<UserResponseDTO.ChangeInfo> updateUserProfile(
            @AuthenticatedUser User user,
            @RequestBody UserRequestDTO.ChangeInfo request
    ){
        User updatedUser = userCommandService.changeUserInfo(user.getId(), request);
        return ApiResponse.onSuccess(UserConverter.toChangeInfo(updatedUser));
    }

    @Operation(summary = "사용자 정보 조회 API", description = "마이페이지에서 볼 수 있는 사용자 정보 조회 API")
    @GetMapping("/profile")
    public ApiResponse<UserResponseDTO.UserProfileDTO> getUserProfile(@AuthenticatedUser User user) {
        return ApiResponse.onSuccess(UserConverter.toUserProfileDTO(user));
    }

}
