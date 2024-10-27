package com.blog.domain.common.exception.user;

import com.blog.domain.common.exception.GlobalException;

import static com.blog.domain.common.exception.GlobalExceptionConst.DUPLICATE_LOGIN_EMAIL;

public class DuplicateLoginEmailException extends GlobalException {

    public DuplicateLoginEmailException() {
        super(DUPLICATE_LOGIN_EMAIL);
    }
}
