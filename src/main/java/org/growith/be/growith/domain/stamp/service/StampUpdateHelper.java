package org.growith.be.growith.domain.stamp.service;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.stamp.entity.enums.StampType;
import org.springframework.stereotype.Component;

/**
 * 
 * // 1. 회원가입 시 (AuthService 등에서)
 * stampUpdateHelper.updateWelcomeStamp(userId);
 * 
 * // 2. 스터디 생성 시 (StudyCommandService에서)
 * stampUpdateHelper.updateLeaderStamp(userId, createdStudyCount);
 * 
 * // 3. 학습일지 작성 시 (JournalService에서)
 * stampUpdateHelper.updateRecordStamp(userId, journalCount);
 * 
 * // 4. 응원하기 시 (CheerService에서)
 * stampUpdateHelper.updateCheerStamp(userId, cheerGivenCount);
 * 
 * // 5. 응원 받기 시 (CheerService에서)
 * stampUpdateHelper.updateSuperstarStamp(userId, cheerReceivedCount);
 */
@Component
@RequiredArgsConstructor
public class StampUpdateHelper {

    private final StampService stampService;

    /**
     * 웰컴숙 스탬프 업데이트 (회원가입)
     */
    public void updateWelcomeStamp(Long userId) {
        stampService.updateStampProgress(userId, StampType.WELCOME, 1);
    }

    /**
     * 리더숙 스탬프 업데이트 (스터디 개설)
     * @param userId 사용자 ID
     * @param studyCount 개설한 스터디 총 개수
     */
    public void updateLeaderStamp(Long userId, int studyCount) {
        stampService.updateStampProgress(userId, StampType.LEADER, studyCount);
    }

    /**
     * 기록숙 스탬프 업데이트 (학습일지 작성)
     * @param userId 사용자 ID
     * @param journalCount 작성한 학습일지 총 개수
     */
    public void updateRecordStamp(Long userId, int journalCount) {
        stampService.updateStampProgress(userId, StampType.RECORD, journalCount);
    }

    /**
     * 응원 고숙 스탬프 업데이트 (응원하기 진행)
     * @param userId 사용자 ID
     * @param cheerGivenCount 응원한 사람 수
     */
    public void updateCheerStamp(Long userId, int cheerGivenCount) {
        stampService.updateStampProgress(userId, StampType.CHEER, cheerGivenCount);
    }

    /**
     * 슈퍼숙타 스탬프 업데이트 (응원 받기)
     * @param userId 사용자 ID
     * @param cheerReceivedCount 받은 응원 총 개수
     */
    public void updateSuperstarStamp(Long userId, int cheerReceivedCount) {
        stampService.updateStampProgress(userId, StampType.SUPERSTAR, cheerReceivedCount);
    }
}
