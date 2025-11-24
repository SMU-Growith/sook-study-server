package org.growith.be.growith.domain.journal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.growith.be.growith.global.common.BaseEntity;

@Entity
@Table(name = "study_journal")
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyJournal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String url;

    // 파일 다운로드 경로 (S3 URL 또는 로컬 경로 등)
    private String fileUrl;

    // 실제 업로드된 파일 이름
    private String fileName;

    private Long sessionId;

    private Long userId;

    public static StudyJournal createJournal(
            String title,
            String content,
            String url,
            String fileUrl,
            String fileName,
            Long sessionId,
            Long userId
    ) {
        return StudyJournal.builder()
                .title(title)
                .content(content)
                .url(url)
                .fileUrl(fileUrl)
                .fileName(fileName)
                .sessionId(sessionId)
                .userId(userId)
                .build();
    }

    public void updateJournal(String title, String content, String url, String fileUrl, String fileName) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }
}
