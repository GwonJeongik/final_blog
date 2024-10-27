package com.blog.domain.user.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserStatus {

    ACTIVE("활성화"),
    DELETE("삭제");

    private final String description;
}
