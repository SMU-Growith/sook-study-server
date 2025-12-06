package org.growith.be.growith.domain.study.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.enums.StudyFormat;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.study.entity.enums.StudyStyleCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.growith.be.growith.domain.study.entity.QStudy.study;

@Repository
@RequiredArgsConstructor
public class StudyQueryDslImpl implements StudyQueryDsl {

    private JPAQueryFactory queryFactory;

    public List<Study> searchStudy(StudyRequestDto.SearchStudyCondition request, PageRequest pageRequest){
        return queryFactory.selectFrom(study)
                .where(
                    studyStyleIn(request.studyStyleCategories()),
                    studyFormatIn(request.studyFormats()),
                    studyStatusEq(request.studyStatus()),
                    contentContains(request.searchContent())
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(study.createdAt.desc(),
                        study.scrapCount.desc()
                )
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

    private BooleanExpression studyStatusEq(StudyStatus status) {
        if (status == null) {
            return null;
        }
        return study.studyStatus.eq(status);
    }

    private BooleanExpression contentContains(String searchContent) {
        if (searchContent == null || searchContent.isBlank()) {
            return null;
        }
        return study.title.containsIgnoreCase(searchContent);
    }

}
