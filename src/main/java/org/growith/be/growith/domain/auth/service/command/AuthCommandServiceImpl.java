package org.growith.be.growith.domain.auth.service.command;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.auth.converter.AuthConverter;
import org.growith.be.growith.domain.auth.dto.request.AuthRequestDTO;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.AuthErrorCode;
import org.growith.be.growith.global.error.exception.handler.AuthException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final UserRepository userRepository;

    @Override
    public void signUp(AuthRequestDTO.SignUp request){
        validateSignUp(request);
        userRepository.save(AuthConverter.toLocalUser(request));
    }




    private void validateSignUp(AuthRequestDTO.SignUp request) throws AuthException{
        if (userRepository.existsByEmail(request.email())){
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_EMAIL);
        }
    }
}
