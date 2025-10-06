package org.growith.be.growith.domain.study.service;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.study.dto.StudyCardDto;
import org.growith.be.growith.domain.study.dto.StudyDtlDto;
import org.growith.be.growith.domain.study.entity.*;
import org.growith.be.growith.domain.study.entity.enums.ContactType;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.domain.study.repository.StudyFieldRepository;
import org.growith.be.growith.domain.study.repository.StyleRepository;
import org.growith.be.growith.domain.study.repository.RuleRepository;
import org.growith.be.growith.domain.study.repository.StudyStyleRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyFieldRepository studyFieldRepository;
    private final StyleRepository styleRepository;
    private final RuleRepository ruleRepository;

    private final StudyStyleRepository studyStyleRepository;

    public List<StudyCardDto> getPopularStudies(int page, int size) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return studyRepository.findPopularStudies(oneMonthAgo, PageRequest.of(page, size));
    }

    public List<StudyCardDto> getNewStudies(int page, int size) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return studyRepository.findNewStudies(oneMonthAgo, PageRequest.of(page, size));
    }

    @Transactional
    public void createStudy(StudyDtlDto dto) {
        // 작성자 조회
        User user = userRepository.findByUserId(dto.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 분야 조회
        StudyField field = studyFieldRepository.findByName(dto.getFieldName())
                .orElseThrow(() -> new IllegalArgumentException("분야 없음"));

        // Study 생성
        Study study = Study.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .studyStatus(StudyStatus.ACTIVE) // 기본값 ACTIVE
                .contactType(ContactType.valueOf(dto.getContactType()))
                .user(user)
                .studyField(field)
                .format(Format.valueOf(dto.getFormat().toUpperCase()))
                .build();
        studyRepository.save(study);

        // 스타일 저장
        if (dto.getStyleNames() != null) {
            List<Style> styles = styleRepository.findByStyleNameIn(dto.getStyleNames());
            for (Style style : styles) {
                StudyStyle studyStyle = StudyStyle.builder()
                        .study(study)
                        .style(style)
                        .build();
                studyStyleRepository.save(studyStyle);
            }
        }

        // 규칙 저장
        if (dto.getRules() != null) {
            dto.getRules().forEach((category, desc) -> {
                Rule rule = Rule.builder()
                        .study(study)
                        .ruleCategory(category)
                        .description(desc)
                        .build();
                ruleRepository.save(rule);
            });
        }
    }

    public StudyDtlDto getStudyDetail(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디 없음"));

        // 스타일 이름 리스트
        List<String> styleNames = study.getStudyStyles().stream()
                .map(ss -> ss.getStyle().getStyleName())
                .toList();

        // 규칙 맵
        List<Rule> rules = ruleRepository.findByStudy(study);
        Map<String, String> ruleMap = rules.stream()
                .collect(java.util.stream.Collectors.toMap(Rule::getRuleCategory, Rule::getDescription));

        return StudyDtlDto.builder()
                .fieldName(study.getStudyField().getName())
                .styleNames(styleNames)
                .format(study.getFormat())
                .contactType(study.getContactType().name())
                .title(study.getTitle())
                .description(study.getDescription())
                .rules(ruleMap)
                .authorId(study.getUser().getUserId())
                .createdAt(study.getCreatedAt())
                .build();
    }

    public StudyDtlDto updateStudy(Long studyId, StudyDtlDto dto) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("studyId에 해당하는 스터디 없음"));


        // 분야 조회
        //사용자가 보내준 수정값(분야)가 db에 있는지 확인
        StudyField field = studyFieldRepository.findByName(dto.getFieldName())
                .orElseThrow(() -> new IllegalArgumentException("분야 없음"));


        // 스터디 정보 업데이트
        study.setTitle(dto.getTitle());
        study.setDescription(dto.getDescription());
        study.setContactType(ContactType.valueOf(dto.getContactType()));
        study.setStudyField(field);
        study.setFormat(Format.valueOf(dto.getFormat().toUpperCase()));
        studyRepository.save(study);

        // 기존 스타일 삭제 후 새로 저장
        studyStyleRepository.deleteByStudy(study);
        if (dto.getStyleNames() != null) {
            List<Style> styles = styleRepository.findByStyleNameIn(dto.getStyleNames());
            for (Style style : styles) {
                StudyStyle studyStyle = StudyStyle.builder()
                        .study(study)
                        .style(style)
                        .build();
                studyStyleRepository.save(studyStyle);
            }
        }

        // 기존 규칙 삭제 후 새로 저장
        ruleRepository.deleteByStudy(study);
        if (dto.getRules() != null) {
            dto.getRules().forEach((category, desc) -> {
                Rule rule = Rule.builder()
                        .study(study)
                        .ruleCategory(category)
                        .description(desc)
                        .build();
                ruleRepository.save(rule);
            });
        }

        return getStudyDetail(studyId);
    }

    public StudyDtlDto closedStudy(Long studyId)
    {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("studyId에 해당하는 스터디 없음"));
        study.setStudyStatus(StudyStatus.CLOSED);
        studyRepository.save(study);
        return getStudyDetail(studyId);
    }

    public void deleteStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("studyId에 해당하는 스터디 없음"));
        studyRepository.delete(study);
    }

    public List<StudyCardDto> searchStudies(
            List<String> fields,
            List<String> formats,
            List<String> styles,
            String status,
            String keyword,
            String sort,
            int page,
            int size
    ) {
        Specification<Study> spec = StudySpecifications.searchSpec(fields, formats, styles, status, keyword);
        Sort sortObj = ("old".equalsIgnoreCase(sort)) ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        PageRequest pageable = PageRequest.of(page, size, sortObj);
        return studyRepository.findAll(spec, pageable)
                .stream()
                .map(study -> StudyCardDto.builder()
                        .studyId(study.getId())
                        .studyStatus(study.getStudyStatus())
                        .title(study.getTitle())
                        .authorId(study.getUser().getUserId())
                        .scrapCount(0L) // 0으로 하드코딩해둠. 추후에 스크랩 수를 계산하는 로직 추가 필요
                        .format(study.getFormat() != null ? study.getFormat().name() : null)
                        .fieldName(study.getStudyField().getName())
                        .styleNames(study.getStudyStyles().stream().map(ss -> ss.getStyle().getStyleName()).toList())
                        .build())
                .toList();
    }
}