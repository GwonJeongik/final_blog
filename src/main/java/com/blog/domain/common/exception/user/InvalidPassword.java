package com.blog.domain.common.exception.user;

import com.blog.domain.common.exception.GlobalException;

import static com.blog.domain.common.exception.GlobalExceptionConst.INVALID_PASSWORD;

public class InvalidPassword extends GlobalException {

    public InvalidPassword() {
        super(INVALID_PASSWORD);
    }
}
