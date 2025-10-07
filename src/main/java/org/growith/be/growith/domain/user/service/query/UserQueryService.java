package org.growith.be.growith.domain.user.service.query;

import org.growith.be.growith.domain.user.entity.User;

public interface UserQueryService {
    User findById(Long id);
}
