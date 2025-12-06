package org.growith.be.growith.domain.user.dto.request;

import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

public record UserRequestDTO() {

    public record ChangeInfo(
            String nickName,
            StudentStatus studentStatus,
            Major major,
            String phoneNumber
//            String studyStyle
    ){}
}
