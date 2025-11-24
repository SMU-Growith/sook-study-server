package org.growith.be.growith.domain.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.growith.be.growith.domain.application.entity.ApplicationStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequest {
    private ApplicationStatus status;
}
