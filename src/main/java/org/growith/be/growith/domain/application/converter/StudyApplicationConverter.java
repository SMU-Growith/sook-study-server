package org.growith.be.growith.domain.application.converter;

import org.growith.be.growith.domain.application.dto.request.StudyApplicationRequestDTO;
import org.growith.be.growith.domain.application.dto.response.StudyApplicationResponseDTO;
import org.growith.be.growith.domain.application.entity.StudyApplication;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.user.entity.User;

import java.util.List;

public class StudyApplicationConverter {

    // StudyApplicationRequestDTO.CreateStudyApplicationDTO -> Application
    public static StudyApplication toStudyApplicationEntity(StudyApplicationRequestDTO.CreateStudyApplicationDTO dto, User user, Study study) {
        return StudyApplication.builder()
                .nickname(user.getNickName())
                .studentStatus(dto.studentStatus())
                .major(dto.major())
                .phoneNumber(dto.phoneNumber())
                .motivation(dto.motivation())
                .applicationStatus(dto.applicationStatus())
                .user(user)
                .study(study)
                .build();
    }

    // Application -> StudyApplicationResponseDTO.StudyApplicationDetailDTO
    public static StudyApplicationResponseDTO.StudyApplicationDetailDTO toApplicationDetailDTO(StudyApplication studyApplication){
        return StudyApplicationResponseDTO.StudyApplicationDetailDTO.builder()
                .applicationId(studyApplication.getId())
                .studyId(studyApplication.getStudy().getId())
                .userId(studyApplication.getUser().getId())
                .nickName(studyApplication.getNickname())
                .studentStatus(studyApplication.getStudentStatus())
                .major(studyApplication.getMajor())
                .phoneNumber(studyApplication.getPhoneNumber())
                .motivation(studyApplication.getMotivation())
                .applicationStatus(studyApplication.getApplicationStatus())
                .build();
    }

    // List<Application> -> List<StudyApplicationResponseDTO.StudyApplicationDetailDTO>
    public static List<StudyApplicationResponseDTO.StudyApplicationDetailDTO> toApplicationDetailDTOList(List<StudyApplication> studyApplications){
        return studyApplications.stream()
                .map(StudyApplicationConverter::toApplicationDetailDTO)
                .toList();
    }
    public static StudyApplicationResponseDTO.UpdateApplicationResponseDTO toUpdateApplicationResponseDTO(StudyApplication studyApplication){
        return StudyApplicationResponseDTO.UpdateApplicationResponseDTO.builder()
                .applicationId(studyApplication.getId())
                .studyId(studyApplication.getStudy().getId())
                .applicationStatus(studyApplication.getApplicationStatus())
                .build();
    }

    public static StudyApplicationResponseDTO.MyApplicationCardDTO toMyApplicationCardDTO(
            StudyApplication application, 
            Boolean scrapped
    ) {
        Study study = application.getStudy();
        
        return StudyApplicationResponseDTO.MyApplicationCardDTO.builder()
                .applicationId(application.getId())
                .studyId(study.getId())
                .title(study.getTitle())
                .studyStatus(study.getStudyStatus())
                .studyFormat(study.getStudyFormat().name())
                .studyFieldName(study.getStudyField().getName())
                .studyStyleCategory(study.getStudyStyleCategory().name())
                .nickname(application.getNickname())
                .scrapCount(study.getScrapCount())
                .isScraped(scrapped)
                .createdAt(application.getCreatedAt())
                .applicationStatus(application.getApplicationStatus())
                .build();
    }

    public static List<StudyApplicationResponseDTO.MyApplicationCardDTO> toMyApplicationCardDTOList(
            List<StudyApplication> applications,
            List<Boolean> scrappedList
    ) {
        List<StudyApplicationResponseDTO.MyApplicationCardDTO> result = new java.util.ArrayList<>();
        for (int i = 0; i < applications.size(); i++) {
            result.add(toMyApplicationCardDTO(applications.get(i), scrappedList.get(i)));
        }
        return result;
    }
}
