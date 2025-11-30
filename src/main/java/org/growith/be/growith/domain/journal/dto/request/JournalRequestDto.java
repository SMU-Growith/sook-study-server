package org.growith.be.growith.domain.journal.dto.request;

public class JournalRequestDto {

    public static class createJournalDto {
        private String title;
        private String content;
        private String url;
        private String fileUrl;
        private String fileName;
        private Long sessionId;
        private Long userId;
    }
}
