package org.growith.be.growith.domain.study.converter;

import org.growith.be.growith.domain.journal.dto.request.JournalRequestDto;
import org.growith.be.growith.domain.journal.entity.StudyJournal;
import org.growith.be.growith.domain.study.dto.request.StudyRequestDto;
import org.growith.be.growith.domain.study.dto.response.StudyResponseDto;
import org.growith.be.growith.domain.study.entity.*;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.user.entity.User;

import java.util.List;

public class StudyConverter {

    // StudyRequestDto.CreateStudyDTO -> Study
    public static Study toStudyEntity(StudyRequestDto.CreateStudyDTO dto, User user, StudyField studyField) {

        return Study.builder()
                .title(dto.title())
                .description(dto.description())
                .studyStatus(StudyStatus.ACTIVE)
                .contactType(dto.contactType())
                .url(dto.url())
                .isRecruiting(true)
                .studyFormat(dto.studyFormat())
                .studyStyleCategory(dto.studyStyleCategory())
                .user(user)
                .studyField(studyField)
                .build();
    }

    // CreateRuleDTO -> Rule
    public static Rule toRuleEntity(StudyRequestDto.RuleDTO dto, Study study){
        return Rule.builder()
                .study(study)
                .ruleCategory(dto.ruleCategory())
                .description(dto.description())
                .build();
    }

    public static StudyResponseDto.StudyDetail toStudyDetail(Study study, List<Rule> rules){
        List<StudyResponseDto.RuleDetailDTO> ruleList = rules.stream()
                .map(StudyConverter::toRuleDetailDTO)
                .toList();

        return StudyResponseDto.StudyDetail.builder()
                .title(study.getTitle())
                .description(study.getDescription())
                .studyStatus(study.getStudyStatus())
                .contactType(study.getContactType())
                .url(study.getUrl())
                .isRecruiting(study.getIsRecruiting())
                .studyFieldId(study.getStudyField().getId())
                .studyFieldName(study.getStudyField().getName())
                .studyFormat(study.getStudyFormat())
                .studyStyleCategory(study.getStudyStyleCategory())
                .ruleDTO(ruleList)
                .userId(study.getUser().getId())
                .createdAt(study.getCreatedAt().toLocalDate())
                .build();
    }

    public static StudyResponseDto.RuleDetailDTO toRuleDetailDTO(Rule rule){
        return StudyResponseDto.RuleDetailDTO.builder()
                .ruleCategory(rule.getRuleCategory())
                .description(rule.getDescription())
                .build();
    }

    /*
    * // Study 생성
        Study study = Study.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .studyStatus(StudyStatus.ACTIVE) // 기본값 ACTIVE
                .contactType(ContactType.valueOf(request.getContactType()))
                .user(user)
                .studyField(field)
                .format(Format.valueOf(request.getFormat().toUpperCase()))
                .build();
    *
    * */

/*
    // Study -> StudyCardDto
    public static StudyResponseDto.StudyCardDto toStudyCardDto(Study study, Integer memberCount, Integer studyDays) {
        List<String> list = study.getStudyStyles().stream().map(ss -> ss.getStyle().getStyleName()).toList();

        return StudyResponseDto.StudyCardDto.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .format(study.getStudyFormat() != null ? study.getStudyFormat().name() : null)
                .fieldName(study.getStudyField().getName())
                .styleNames(list)
                .memberCount(memberCount)
                .studyDays(studyDays)
                .build();
    }

 */

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

    // ChangedStudyStatus
    public static StudyResponseDto.ChangedStudyStatus toChangedStudyStatus(StudyStatus studyStatus){
        return StudyResponseDto.ChangedStudyStatus.builder()
                .studyStatus(studyStatus)
                .build();
    }

    //  Study  -> StudyResponseDto.StudyPreviewDTO
    public static StudyResponseDto.StudyPreviewDTO toStudyPreviewDTO(Study study){
        Boolean isScraped = study.getScrapCount() != 0;

        return StudyResponseDto.StudyPreviewDTO.builder()
                .studyId(study.getId())
                .studyStatus(study.getStudyStatus())
                .title(study.getTitle())
                .userId(study.getUser().getId())
                .url(study.getUrl())
                .scrapCount(study.getScrapCount())
                .isScraped(isScraped)
                .studyFormat(study.getStudyFormat())
                .studyField(study.getStudyField())
                .studyStyleCategory(study.getStudyStyleCategory())
                .build();
    }


    // StudyResponseDto.StudyPreviewDTO
    // -> StudyResponseDto.StudyPreviewDTOList
    public static StudyResponseDto.StudyPreviewDTOList toStudyPreviewDTOList(List<Study> studies){
        List<StudyResponseDto.StudyPreviewDTO> list = studies.stream()
                .map(StudyConverter::toStudyPreviewDTO)
                .toList();

        return StudyResponseDto.StudyPreviewDTOList.builder()
                .studyPreviews(list)
                .listSize(list.size())
                .build();
    }

    // StudyResponseDto.UserStudyPreviewDto
    public static StudyResponseDto.UserStudyPreviewDto toUserStudyPreviewDto(UserStudy dto, Long memberCount, Long studySessionCount){
        return StudyResponseDto.UserStudyPreviewDto.builder()
                .studyId(dto.getStudy().getId())
                .studyRole(dto.getStudyRole())
                .title(dto.getStudy().getTitle())
                .studyStatus(dto.getStudy().getStudyStatus())
                .userId(dto.getUser().getId())
                .url(dto.getStudy().getUrl())
                .memberCount(memberCount)
                .studySessionCount(studySessionCount)
                .studyFormat(dto.getStudy().getStudyFormat())
                .studyFieldId(dto.getStudy().getStudyField().getId())
                .studyFieldName(dto.getStudy().getStudyField().getName())
                .studyStyleCategory(dto.getStudy().getStudyStyleCategory())
                .build();
    }

    // StudyUsers
    public static StudyResponseDto.StudyUsers toStudyUsers(UserStudy userStudy, String motivation){
        return StudyResponseDto.StudyUsers.builder()
                .userId(userStudy.getUser().getId())
                .studyRole(userStudy.getStudyRole())
                .nickName(userStudy.getUser().getNickName())
                .studentStatus(userStudy.getUser().getStudentStatus())
                .phoneNumber(userStudy.getUser().getPhoneNumber())
                .motivation(motivation)
                .build();
    }
}
