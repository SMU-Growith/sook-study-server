package org.growith.be.growith.domain.auth.service.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.converter.AuthConverter;
import org.growith.be.growith.domain.auth.dto.request.AuthRequestDTO;
import org.growith.be.growith.domain.auth.dto.response.AuthResponseDTO;
import org.growith.be.growith.domain.auth.service.query.TokenQueryService;
import org.growith.be.growith.domain.auth.service.query.TokenStorageQueryService;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.AuthErrorCode;
import org.growith.be.growith.global.error.code.status.TokenErrorCode;
import org.growith.be.growith.global.error.code.status.UserErrorCode;
import org.growith.be.growith.global.error.exception.handler.AuthException;
import org.growith.be.growith.global.error.exception.handler.TokenException;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.growith.be.growith.global.security.constants.AuthenticationConstants;
import org.growith.be.growith.global.security.domain.CustomUserDetails;
import org.growith.be.growith.global.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenQueryService tokenQueryService;
    private final TokenCommandService tokenCommandService;
    private final TokenStorageQueryService tokenStorageQueryService;
    private final TokenStorageCommandService tokenStorageCommandService;
    private final JwtUtil jwtUtil;


    @Override
    public AuthResponseDTO.TokenResult signUp(AuthRequestDTO.SignUp request){
        validateSignUp(request);

        User user = AuthConverter.toLocalUser(request);
        user.encodePassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(savedUser);
        return tokenCommandService.createLoginToken(customUserDetails);
    }

    @Override
    public AuthResponseDTO.TokenResult login(AuthRequestDTO.Login request){
        User user = userRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.INCORRECT_PASSWORD);
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        AuthResponseDTO.TokenResult loginToken = tokenCommandService.createLoginToken(customUserDetails);
        tokenStorageCommandService.addRefreshToken(user.getId(), loginToken.refreshToken());
        return loginToken;
    }

    @Override
    public AuthResponseDTO.AccessTokenResult reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = JwtUtil.resolveToken(request);
        Long userId = getUserId(refreshToken);
        if (!jwtUtil.validateToken(refreshToken) || userId == null || !tokenStorageQueryService.getRefreshToken(userId).equals(refreshToken)){
            throw new TokenException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String accessToken = tokenCommandService.reissueAccessToken(customUserDetails);
        return AuthConverter.toAccessTokenResult(user.getId(), accessToken);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = JwtUtil.resolveToken(request);

        tokenStorageCommandService.addBlackList(accessToken);
    }


    private void validateSignUp(AuthRequestDTO.SignUp request) throws AuthException{
        // 아이디 중복 검사
        if (userRepository.existsByLoginId(request.loginId())){
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_LOGIN_ID);
        }
        // 이메일 중복 검사
        else if (userRepository.existsByEmail(request.email())){
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_EMAIL);
        }
    }

    private Long getUserId(String token){
        return tokenQueryService.getUserId(token);
    }
}
