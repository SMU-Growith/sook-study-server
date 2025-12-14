package org.growith.be.growith.domain.study.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.StudyField;
import org.growith.be.growith.domain.study.entity.enums.StudyFormat;
import org.growith.be.growith.domain.study.entity.enums.StudyStyleCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.growith.be.growith.domain.study.entity.QStudy.study;

@Repository
@RequiredArgsConstructor
public class StudyQueryDslImpl implements StudyQueryDsl {

    private final JPAQueryFactory queryFactory;

    public List<Study> searchStudy(StudyRequestDto.SearchStudyCondition request, List<StudyField> studyFields, PageRequest pageRequest){
        return queryFactory.selectFrom(study)
                .where(
                    studyStyleIn(request.studyStyleCategories()),
                    studyFormatIn(request.studyFormats()),
                    isRecruitingEq(request.isRecruiting()),
                    contentContains(request.searchContent()),
                    studyFieldIn(studyFields)
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(study.createdAt.desc(),
                        study.scrapCount.desc()
                )
                .fetch();
    }

    public List<Study> getStudySortByPopularOrNew(Pageable pageable){
        return queryFactory.selectFrom(study)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .fetch();
    }


    private BooleanExpression studyStyleIn(List<StudyStyleCategory> studyStyleCategories){
        if (studyStyleCategories == null || studyStyleCategories.isEmpty()){
            return null;
        }
        return study.studyStyleCategory.in(studyStyleCategories);
    }

    private BooleanExpression studyFormatIn(List<StudyFormat> studyFormats){
        if (studyFormats == null || studyFormats.isEmpty()){
            return null;
        }
        return study.studyFormat.in(studyFormats);
    }

    private BooleanExpression isRecruitingEq(Boolean isRecruiting) {
        if (isRecruiting == null) {
            return null;  // null이면 전체 조회
        }
        return study.isRecruiting.eq(isRecruiting);
    }

    private BooleanExpression contentContains(String searchContent) {
        if (searchContent == null || searchContent.isBlank()) {
            return null;
        }
        return study.title.containsIgnoreCase(searchContent);
    }

    private BooleanExpression studyFieldIn(List<StudyField> studyFields) {
        if (studyFields == null || studyFields.isEmpty()) {
            return null;
        }
        return study.studyField.in(studyFields);
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort sort) {
        // sort=createdAt,desc 또는 sort=scrapCount,asc 이런 식으로 온다고 가정
        if (sort.isUnsorted()) {
            // 기본 정렬: 최신순
            return new OrderSpecifier<>(Order.DESC, study.createdAt);
        }

        Sort.Order order = sort.iterator().next(); // 첫 번째 것만 사용
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;

        return switch (order.getProperty()) {
            case "createdAt" -> new OrderSpecifier<>(direction, study.createdAt);
            case "scrapCount" -> new OrderSpecifier<>(direction, study.scrapCount);
            default -> new OrderSpecifier<>(Order.DESC, study.createdAt); // 안전장치
        };
    }


}
