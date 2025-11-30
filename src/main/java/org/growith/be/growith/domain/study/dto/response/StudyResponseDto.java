package org.growith.be.growith.domain.study.dto.response;

import lombok.Builder;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.user.entity.enums.Major;
import org.growith.be.growith.domain.user.entity.enums.StudentStatus;

import java.util.List;

public class StudyResponseDto {

    @Builder
    public static class StudyCardDto { // record -> StudyCardDto.getStudyId get
        private Long studyId;
        private StudyStatus studyStatus;
        private String title;
        private String authorId;
        private Long scrapCount;
        private String format; // 진행방식
        private String fieldName; // 분야
        private List<String> styleNames;

        // 내 스터디 조회에서 사용
        private Integer memberCount;    // 스터디원 수
        private Integer studyDays;  // 스터디 진행 일수
        private String userRole; // 사용자의 스터디 내 역할
    }

    @Builder
    public static class StudyMemberDto{
        private Long userId;
        private String nickName;
        private StudentStatus studentStatus;
        private Major major;
        private String phoneNumber;
        private String motivation;
    }

}
