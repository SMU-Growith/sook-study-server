package org.growith.be.growith.domain.journal.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;

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
    private String nickName;
    private StudyRole studyRole;
    private Integer submittedCount;
    private Long viewCount;
    private List<AttachmentDto> attachments;  // 여러 파일
    private EmojiCounts emojiCounts;
    private EmojiStatus emojiStatus; // 내 이모티콘 상태

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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmojiStatus {
        private Boolean heart;
        private Boolean like;
        private Boolean laugh;
        private Boolean surprise;
        private Boolean curiosity;
    }
}