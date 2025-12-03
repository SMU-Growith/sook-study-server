package org.growith.be.growith.global.error.code.status;

import lombok.AllArgsConstructor;
import org.growith.be.growith.global.error.code.BaseErrorCode;
import org.growith.be.growith.global.error.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum TokenErrorCode implements BaseErrorCode {

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN401_1", "토큰의 기한이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401_2", "리프레시 토큰이 유효하지 않습니다."),
    NOT_EXISTS_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN400_1", "토큰이 존재하지 않습니다.")
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
