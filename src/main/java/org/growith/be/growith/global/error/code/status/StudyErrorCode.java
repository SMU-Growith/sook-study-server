package org.growith.be.growith.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.growith.be.growith.global.error.code.BaseErrorCode;
import org.growith.be.growith.global.error.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StudyErrorCode implements BaseErrorCode {

    STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDY_400_1", "해당 스터디를 찾을 수 없습니다."),
    STUDY_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "STUDY_403_1", "해당 스터디를 수정할 권한이 없습니다."),
    STUDY_WITHDRAW_FORBIDDEN(HttpStatus.FORBIDDEN, "STUDY_403_2", "해당 스터디를 탈퇴할 권한이 없습니다."),

    STUDY_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDY_APPLICATION_400_1", "해당 스터디 지원서를 찾을 수 없습니다."),
    STUDY_APPLICATION_FORBIDDEN(HttpStatus.FORBIDDEN, "STUDY_APPLICATION_403_1",
            "해당 스터디 지원서를 승인/거절할 권한이 없습니다."),

    STUDY_FIELD_NOT_FOUND(HttpStatus.NOT_FOUND,"STUDY_FIELD_400_1", "해당 스터디 필드를 찾을 수 없습니다."),

    STUDY_STYLE_NOT_FOUND(HttpStatus.NOT_FOUND,"STUDY_STYLE_400_1", "해당 스터디 성향 스타일을 찾을 수 없습니다."),

    STUDY_SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND,"STUDY_SCRAP_400_1", "해당 스터디 스크랩을 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(httpStatus)
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }
}
