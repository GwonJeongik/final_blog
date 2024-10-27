package com.blog.domain.common.exception.user;

import com.blog.domain.common.exception.GlobalException;

import static com.blog.domain.common.exception.GlobalExceptionConst.NOT_FOUND_USER;

public class NotFoundUserException extends GlobalException {

    public NotFoundUserException() {
        super(NOT_FOUND_USER);
    }
}
