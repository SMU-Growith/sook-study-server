package org.growith.be.growith.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.growith.be.growith.global.error.code.BaseErrorCode;
import org.growith.be.growith.global.error.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {


    NOT_FOUND_LOGIN_MEMBER(HttpStatus.NOT_FOUND, "AUTH404_1", "해당 아이디를 가진 사용자를 찾을 수 없습니다."),
    FAIL_AUTH_LOGIN(HttpStatus.UNAUTHORIZED, "AUTH401_2", "일반 로그인에 실패했습니다."),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH401_3", "비밀번호가 맞지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH401_4", "인증이 필요합니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "AUTH400_1", "이미 존재하는 이메일입니다."),
    ALREADY_EXIST_LOGIN_ID(HttpStatus.BAD_REQUEST, "AUTH400_2", "이미 존재하는 로그인 아이디입니다."),
    ONLY_AVAILABLE_SOCIAL(HttpStatus.BAD_REQUEST, "AUTH400_3", "소셜 로그인만 가능합니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH400_4", "비밀번호를 다시 입력해주세요.")
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
