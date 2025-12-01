package org.growith.be.growith.domain.user.service.command;

import org.growith.be.growith.domain.user.dto.request.UserRequestDTO;
import org.growith.be.growith.domain.user.entity.User;

public interface UserCommandService {
    public User changeUserInfo(Long userId, UserRequestDTO.ChangeInfo request);
}
