package com.blog.domain.user.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {

    USER("사용자"),
    ADMIN("관리자");

    private final String description;
}
