package org.growith.be.growith.domain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class applicationDto {
    private Long applicationId;
    private Long studyId;
    private Long userId;
    private String nickName;
    private String major;
    private String studentStatus;
    private
    private ApplicationStatus applicationStatus;
    private String motivation;
}