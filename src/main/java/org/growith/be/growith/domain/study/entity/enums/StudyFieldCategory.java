package org.growith.be.growith.domain.study.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum StudyFieldCategory {
    // 학업
    MAJOR_STUDY("전공 공부", ParentCategory.ACADEMIC),
    EXAM_STUDY("시험 공부", ParentCategory.ACADEMIC),
    CERTIFICATE("자격증", ParentCategory.ACADEMIC),
    CIVIL_SERVICE("고시·임용·공무원", ParentCategory.ACADEMIC),
    
    // 언어
    CONVERSATION("회화", ParentCategory.LANGUAGE),
    LANGUAGE_TEST("외국어 시험", ParentCategory.LANGUAGE),
    
    // 취업/커리어
    INTERVIEW_RESUME("면접·자소서", ParentCategory.CAREER),
    DESIGN("디자인", ParentCategory.CAREER),
    IT("IT", ParentCategory.CAREER),
    MARKETING("마케팅", ParentCategory.CAREER),
    CODING("코딩", ParentCategory.CAREER),
    DATA_ANALYSIS("데이터 분석", ParentCategory.CAREER),
    
    // 자기계발
    READING_WRITING("독서·글쓰기", ParentCategory.SELF_DEVELOPMENT),
    EXERCISE("운동", ParentCategory.SELF_DEVELOPMENT),
    PHOTO_VIDEO("사진·영상", ParentCategory.SELF_DEVELOPMENT);

    @JsonValue
    private final String description;
    private final ParentCategory parent;

    @Getter
    @RequiredArgsConstructor
    public enum ParentCategory {
        ACADEMIC("학업"),
        LANGUAGE("언어"),
        CAREER("취업/커리어"),
        SELF_DEVELOPMENT("자기계발");

        @JsonValue
        private final String description;

        @JsonCreator
        public static ParentCategory from(String value) {
            if (value == null) return null;
            for (ParentCategory category : ParentCategory.values()) {
                if (category.name().equalsIgnoreCase(value) || category.getDescription().equals(value)) {
                    return category;
                }
            }
            return null;
        }
    }

    @JsonCreator
    public static StudyFieldCategory from(String value) {
        if (value == null) return null;
        for (StudyFieldCategory field : StudyFieldCategory.values()) {
            if (field.name().equalsIgnoreCase(value) || field.getDescription().equals(value)) {
                return field;
            }
        }
        return null;
    }

    // 특정 부모의 자식 필드 목록 조회
    public static List<StudyFieldCategory> getChildrenOf(ParentCategory parent) {
        return Arrays.stream(StudyFieldCategory.values())
                .filter(field -> field.getParent() == parent)
                .collect(Collectors.toList());
    }

    // 특정 부모 이름으로 자식 필드 목록 조회
    public static List<StudyFieldCategory> getChildrenByParentName(String parentName) {
        ParentCategory parent = ParentCategory.from(parentName);
        if (parent == null) return List.of();
        return getChildrenOf(parent);
    }

    // 모든 부모 카테고리 조회
    public static List<ParentCategory> getAllParents() {
        return Arrays.asList(ParentCategory.values());
    }
}
