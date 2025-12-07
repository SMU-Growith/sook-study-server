package org.growith.be.growith.domain.journal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyJournalDto {
    private Long journalId;
    private String title;
    private String content;
    private String url;
    private Long sessionId;
    private Long userId;
    private Integer submittedCount;
    private Long viewCount;
    private List<AttachmentDto> attachments;  // 여러 파일
    private EmojiCounts emojiCounts;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDto {
        private Long attachmentId;
        private String fileUrl;
        private String fileName;
        private Long fileSize;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiCounts {
        private Long heart;
        private Long like;
        private Long laugh;
        private Long surprise;
        private Long curiosity;
    }
}