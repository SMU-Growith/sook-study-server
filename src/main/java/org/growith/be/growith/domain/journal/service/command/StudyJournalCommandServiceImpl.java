package org.growith.be.growith.domain.journal.service.command;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.domain.journal.dto.CreateStudySessionRequest;
import org.growith.be.growith.domain.journal.dto.CreateStudyJournalRequest;
import org.growith.be.growith.domain.journal.dto.StudyJournalDto;
import org.growith.be.growith.domain.journal.dto.StudySession;
import org.growith.be.growith.domain.journal.entity.StudyJournal;
import org.growith.be.growith.domain.journal.repository.StudyJournalRepository;
import org.growith.be.growith.domain.journal.dto.StudySessionCardDto;
import org.growith.be.growith.domain.journal.dto.UpdateStudyJournalRequest;
import org.growith.be.growith.domain.journal.dto.UpdateStudySessionRequest;
import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.repository.StudyRepository;
import org.growith.be.growith.domain.study.repository.StudySessionRepository;
import org.growith.be.growith.domain.study.entity.enums.StudyRole;
import org.growith.be.growith.domain.user.entity.User;
import org.growith.be.growith.domain.user.repository.UserRepository;
import org.growith.be.growith.global.error.code.status.StudyErrorCode;
import org.growith.be.growith.global.error.exception.handler.StudyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyJournalCommandServiceImpl implements StudyJournalCommandService{
    private final StudySessionRepository studySessionRepository;
    private final StudyJournalRepository studyJournalRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final org.growith.be.growith.domain.study.repository.UserStudyRepository userStudyRepository;
    private final org.growith.be.growith.domain.stamp.service.StampUpdateHelper stampUpdateHelper;

    public StudyJournalDto createStudyJournal(Long sessionId, Long userId, CreateStudyJournalRequest request) {
        // 세션 존재 확인
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

        // 사용자 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

        // 일지 생성 (첨부파일 제외)
        StudyJournal journal = StudyJournal.createJournal(
                request.getContent(),
                request.getUrl(),
                sessionId,
                userId
        );

        StudyJournal savedJournal = studyJournalRepository.save(journal);

        // 첨부파일 처리
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            for (CreateStudyJournalRequest.AttachmentRequest attachmentRequest : request.getAttachments()) {
                org.growith.be.growith.domain.journal.entity.JournalAttachment attachment = 
                    org.growith.be.growith.domain.journal.entity.JournalAttachment.create(
                        savedJournal,
                        attachmentRequest.getFileUrl(),
                        attachmentRequest.getFileName(),
                        attachmentRequest.getFileSize()
                    );
                savedJournal.addAttachment(attachment);
            }
            studyJournalRepository.save(savedJournal);
        }

        // 기록숙 스탬프 업데이트
        long journalCount = studyJournalRepository.countByUserId(userId);
        stampUpdateHelper.updateRecordStamp(userId, (int) journalCount);

        // DTO 변환
        List<StudyJournalDto.AttachmentDto> attachmentDtos = savedJournal.getAttachments().stream()
                .map(att -> StudyJournalDto.AttachmentDto.builder()
                        .attachmentId(att.getId())
                        .fileUrl(att.getFileUrl())
                        .fileName(att.getFileName())
                        .fileSize(att.getFileSize())
                        .build())
                .toList();

        // 역할 조회
        String userRoleStr = studyRepository.findUserRoleInStudy(session.getStudy().getId(), user.getId());
        StudyRole studyRole = (userRoleStr != null) ? StudyRole.valueOf(userRoleStr) : StudyRole.MEMBER;

        return StudyJournalDto.builder()
                .journalId(savedJournal.getId())
                .title(session.getTitle())
                .content(savedJournal.getContent())
                .url(savedJournal.getUrl())
                .nickName(user.getNickName())
                .studyRole(studyRole)
                .viewCount(savedJournal.getViewCount())
                .attachments(attachmentDtos)
                .build();
    }

    public void updateStudySession(Long sessionId, UpdateStudySessionRequest request) {
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

        session.updateTitle(request.getTitle());
        studySessionRepository.save(session);
    }

    public StudySessionCardDto getStudySession(Long sessionId) {
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

        return StudySessionCardDto.builder()
                .sessionId(session.getId())
                .sessionNumber(session.getNumber())
                .title(session.getTitle())
                .build();
    }


    public StudyJournalDto updateStudyJournal(Long journalId, UpdateStudyJournalRequest request) {
        StudyJournal journal = studyJournalRepository.findById(journalId)
                .orElseThrow(() -> new IllegalArgumentException("일지를 찾을 수 없음"));

        // 일지 기본 정보 수정
        journal.updateJournal(
                request.getContent(),
                request.getUrl()
        );

        // 첨부파일 전체 교체 (기존 파일 삭제 후 새로 추가)
        journal.getAttachments().clear();  // orphanRemoval=true로 자동 삭제됨
        
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            for (UpdateStudyJournalRequest.AttachmentRequest attachmentRequest : request.getAttachments()) {
                org.growith.be.growith.domain.journal.entity.JournalAttachment attachment = 
                    org.growith.be.growith.domain.journal.entity.JournalAttachment.create(
                        journal,
                        attachmentRequest.getFileUrl(),
                        attachmentRequest.getFileName(),
                        attachmentRequest.getFileSize()
                    );
                journal.addAttachment(attachment);
            }
        }

        StudyJournal updatedJournal = studyJournalRepository.save(journal);

        // DTO 변환
        List<StudyJournalDto.AttachmentDto> attachmentDtos = updatedJournal.getAttachments().stream()
                .map(att -> StudyJournalDto.AttachmentDto.builder()
                        .attachmentId(att.getId())
                        .fileUrl(att.getFileUrl())
                        .fileName(att.getFileName())
                        .fileSize(att.getFileSize())
                        .build())
                .toList();

        // 사용자 및 세션 조회 (역할 확인용)
        User user = userRepository.findById(updatedJournal.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

        StudySession session = studySessionRepository.findById(updatedJournal.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없음"));

        String userRoleStr = studyRepository.findUserRoleInStudy(session.getStudy().getId(), user.getId());
        StudyRole studyRole = (userRoleStr != null) ? StudyRole.valueOf(userRoleStr) : StudyRole.MEMBER;

        return StudyJournalDto.builder()
                .journalId(updatedJournal.getId())
                .title(session.getTitle())
                .content(updatedJournal.getContent())
                .url(updatedJournal.getUrl())
                .nickName(user.getNickName())
                .studyRole(studyRole)
                .viewCount(updatedJournal.getViewCount())
                .attachments(attachmentDtos)
                .build();
    }

    public void deleteStudyJournal(Long journalId) {
        StudyJournal journal = studyJournalRepository.findById(journalId)
                .orElseThrow(() -> new IllegalArgumentException("일지를 찾을 수 없음"));

        studyJournalRepository.delete(journal);
    }

    public StudySessionCardDto createStudySession(Long studyId, CreateStudySessionRequest request) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));

        // 최대 회차 조회
        Integer maxNumber = study.getStudySessions().stream()
                .mapToInt(StudySession::getNumber)
                .max()
                .orElse(0);

        StudySession newSession = StudySession.createSession(
                maxNumber + 1,
                request.getTitle(),
                study
        );


        StudySession savedSession = studySessionRepository.save(newSession);

        return StudySessionCardDto.builder()
                .sessionId(savedSession.getId())
                .sessionNumber(savedSession.getNumber())
                .title(savedSession.getTitle())
                .submittedCount(0)
                .build();
    }

    @Override
    public void deleteStudySession(Long sessionId, Long userId) {
        // 세션 존재 확인
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다."));

        // 해당 세션의 스터디 ID 조회
        Long studyId = session.getStudy().getId();

        // 팀장 권한 확인
        boolean isLeader = userStudyRepository.existsByStudyIdAndUserIdAndStudyRole(
                studyId, 
                userId, 
                org.growith.be.growith.domain.study.entity.enums.StudyRole.LEADER
        );

        if (!isLeader) {
            throw new StudyException(StudyErrorCode.STUDY_UPDATE_FORBIDDEN);
        }

        // 세션 삭제
        studySessionRepository.delete(session);
    }

}
