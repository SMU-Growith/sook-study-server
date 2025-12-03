package org.growith.be.growith.domain.user.service.command;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.user.dto.request.UserRequestDTO;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.UserErrorCode;
import org.growith.be.growith.global.error.exception.handler.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;

    public User changeUserInfo(Long userId, UserRequestDTO.ChangeInfo request){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_FOUND)
        );
        user.changeUserProfile(request);
        return user;
    }
}
