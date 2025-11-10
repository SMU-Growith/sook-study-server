package org.growith.be.growith.domain.application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.growith.be.growith.domain.application.entity.ApplicationStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {
    private Long applicationId;
    private Long studyId;
    private Long userId;
    private String nickName;
    private String major;
    private String studentStatus;
    private ApplicationStatus applicationStatus;
    private String motivation;
}