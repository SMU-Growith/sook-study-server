package org.growith.be.growith.domain.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyMemberDto {
    private Long userId;
    private String nickName;
    private StudentStatus studentStatus;
    private Major major;
    private String phoneNumber;
    // 지원동기도 추가 예정 - 지원로직 만들고나서 . ..
}
