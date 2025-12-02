package org.growith.be.growith.domain.study.converter;

import org.growith.be.growith.domain.journal.dto.request.JournalRequestDto;
import org.growith.be.growith.domain.journal.entity.StudyJournal;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.UserStudy;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.growith.be.growith.domain.user.entity.User;

import java.util.List;

public class StudyConverter {

    // Study -> StudyCardDto
    public static StudyResponseDto.StudyCardDto toStudyCardDto(Study study, Integer memberCount, Integer studyDays) {
        List<String> list = study.getStudyStyles().stream().map(ss -> ss.getStyle().getStyleName()).toList();

        return StudyResponseDto.StudyCardDto.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .format(study.getFormat() != null ? study.getFormat().name() : null)
                .fieldName(study.getStudyField().getName())
                .styleNames(list)
                .memberCount(memberCount)
                .studyDays(studyDays)
                .build();
    }

    // JournalRequestDto.createJournalDto -> StudyJournal
    public static StudyJournal toJournalEntity(JournalRequestDto.createJournalDto dto){
        return null;
//        return StudyJournal.builder()
//                .title(dto.getTitle())
//                .content(dto.content)
//                .url(dto.url)
//                .fileUrl(dto.fileUrl)
//                .fileName(fileName)
//                .sessionId(sessionId)
//                .userId(userId)
//                .build();
    }

    //  User, Study, StudyRole -> UserStudy
    public static UserStudy toUserStudyEntity(User user, Study study, StudyRole studyRole){
        return UserStudy.builder()
                .user(user)
                .study(study)
                .studyRole(studyRole)
                .build();
    }

}
