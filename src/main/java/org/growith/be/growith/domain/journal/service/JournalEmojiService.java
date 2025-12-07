package org.growith.be.growith.domain.journal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.entity.JournalEmoji;
import org.growith.be.growith.domain.journal.entity.JournalEmoji.EmojiType;
import org.growith.be.growith.domain.journal.repository.JournalEmojiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JournalEmojiService {
    
    private final JournalEmojiRepository journalEmojiRepository;
    private final org.growith.be.growith.domain.journal.repository.StudyJournalRepository studyJournalRepository;
    private final org.growith.be.growith.domain.stamp.service.StampUpdateHelper stampUpdateHelper;

    @Transactional
    public StudyJournalDto.EmojiCounts toggleEmoji(Long studyJournalId, Long userId, String emojiTypeStr) {
        // String을 EmojiType enum으로 변환
        EmojiType requestedEmojiType = EmojiType.valueOf(emojiTypeStr.toUpperCase());
        
        // 사용자가 이미 이 저널에 이모티콘을 눌렀는지 확인
        Optional<JournalEmoji> existingEmoji = journalEmojiRepository.findByStudyJournalIdAndUserId(
                studyJournalId, userId
        );

        if (existingEmoji.isPresent()) {
            JournalEmoji emoji = existingEmoji.get();
            
            // 같은 이모티콘을 다시 클릭 -> 삭제 (토글 off)
            if (emoji.getEmojiType() == requestedEmojiType) {
                journalEmojiRepository.delete(emoji);
                log.info("이모티콘 삭제: journalId={}, userId={}, emojiType={}", 
                        studyJournalId, userId, requestedEmojiType);
            } 
            // 다른 이모티콘 클릭 -> 변경
            else {
                emoji.updateEmojiType(requestedEmojiType);
                journalEmojiRepository.save(emoji);
                log.info("이모티콘 변경: journalId={}, userId={}, {} -> {}", 
                        studyJournalId, userId, emoji.getEmojiType(), requestedEmojiType);
            }
        } else {
            // 새로운 이모티콘 추가
            JournalEmoji newEmoji = JournalEmoji.builder()
                    .studyJournalId(studyJournalId)
                    .userId(userId)
                    .emojiType(requestedEmojiType)
                    .build();
            
            journalEmojiRepository.save(newEmoji);
            log.info("이모티콘 추가: journalId={}, userId={}, emojiType={}", 
                    studyJournalId, userId, requestedEmojiType);
        }

        // 업데이트된 이모티콘 카운트 반환
        updateStamps(userId, studyJournalId);
        return getEmojiCounts(studyJournalId);
    }

    private void updateStamps(Long userId, Long studyJournalId) {
        // 1. 응원 고숙 (내가 응원한 사람 수)
        long cheerGivenCount = journalEmojiRepository.countDistinctCheeredUsers(userId);
        stampUpdateHelper.updateCheerStamp(userId, (int) cheerGivenCount);

        // 2. 슈퍼숙타 (내 일지가 받은 응원 수) -> 일지 주인의 스탬프 업데이트
        org.growith.be.growith.domain.journal.entity.StudyJournal journal = studyJournalRepository.findById(studyJournalId).orElse(null);
        if (journal != null) {
            Long journalOwnerId = journal.getUserId();
            long cheerReceivedCount = journalEmojiRepository.countReceivedCheersByUserId(journalOwnerId);
            stampUpdateHelper.updateSuperstarStamp(journalOwnerId, (int) cheerReceivedCount);
        }
    }

        @Transactional(readOnly = true)
    public StudyJournalDto.EmojiCounts getEmojiCounts(Long studyJournalId) {
        List<Object[]> results = journalEmojiRepository.countEmojisByJournalId(studyJournalId);
        
        // 카운트 초기화
        long heartCount = 0L;
        long likeCount = 0L;
        long laughCount = 0L;
        long surpriseCount = 0L;
        long curiosityCount = 0L;

        // 쿼리 결과를 각 이모티콘 타입별로 매핑
        for (Object[] result : results) {
            EmojiType emojiType = (EmojiType) result[0];
            Long count = (Long) result[1];

            switch (emojiType) {
                case HEART -> heartCount = count;
                case LIKE -> likeCount = count;
                case LAUGH -> laughCount = count;
                case SURPRISE -> surpriseCount = count;
                case CURIOSITY -> curiosityCount = count;
            }
        }

        return StudyJournalDto.EmojiCounts.builder()
                .heart(heartCount)
                .like(likeCount)
                .laugh(laughCount)
                .surprise(surpriseCount)
                .curiosity(curiosityCount)
                .build();
    }

    @Transactional(readOnly = true)
    public StudyJournalDto.EmojiStatus getEmojiStatus(Long studyJournalId, Long userId) {
        if (userId == null) {
            return StudyJournalDto.EmojiStatus.builder()
                    .heart(false).like(false).laugh(false).surprise(false).curiosity(false)
                    .build();
        }

        Optional<JournalEmoji> emojiOpt = journalEmojiRepository.findByStudyJournalIdAndUserId(studyJournalId, userId);

        if (emojiOpt.isEmpty()) {
            return StudyJournalDto.EmojiStatus.builder()
                    .heart(false).like(false).laugh(false).surprise(false).curiosity(false)
                    .build();
        }

        EmojiType type = emojiOpt.get().getEmojiType();
        
        return StudyJournalDto.EmojiStatus.builder()
                .heart(type == EmojiType.HEART)
                .like(type == EmojiType.LIKE)
                .laugh(type == EmojiType.LAUGH)
                .surprise(type == EmojiType.SURPRISE)
                .curiosity(type == EmojiType.CURIOSITY)
                .build();
    }
}