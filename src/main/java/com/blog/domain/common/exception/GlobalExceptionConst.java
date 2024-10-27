package com.blog.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionConst {

    //=== User Exception ===//
    // User_Exception (상태코드 401) //
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // User_Exception (상태코드 404) //
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "이메일이 존재하지 않습니다."),

    // User_Exception (상태코드 409) //
    DUPLICATE_LOGIN_EMAIL(HttpStatus.CONFLICT, "중복된 로그인 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
