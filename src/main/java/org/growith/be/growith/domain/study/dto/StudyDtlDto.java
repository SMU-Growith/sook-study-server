package org.growith.be.growith.domain.study.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class StudyDtlDto {
    private String fieldName; // 스터디 분야
    private List<String> styleNames; // 스터디 스타일
    private String format; // 진행방식
    private String contactType; // 연락 방식
    private String title; // 제목
    private String description; // 스터디 소개
    private Map<String, String> rules; // 규칙: key=rule_category(time, panalty, rest, mood, etc), value=description
    private String authorId; // 글 작성자
    private LocalDateTime createdAt; // 작성 날짜
}

