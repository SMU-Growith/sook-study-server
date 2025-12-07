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
public class UpdateStudyJournalRequest {
    private String title;
    private String content;
    private String url;
    private List<AttachmentRequest> attachments;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentRequest {
        private Long attachmentId;
        private String fileUrl;
        private String fileName;
        private Long fileSize;
    }
}
