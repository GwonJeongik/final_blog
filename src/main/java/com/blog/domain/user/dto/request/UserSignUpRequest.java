package com.blog.domain.user.dto.request;

import com.blog.domain.user.entity.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "이메일 입력은 필수 입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String loginEmail;

    @Pattern(
            regexp = "^(?=.*[A-Za-z\\d])(?=.*[!@#$%^&*])[A-Za-z\\d\\W]{8,}$",
            message = "비밀번호는 특수 문자를 포함 8자리 이상입니다."
    )
    private String password;

    @NotNull(message = "사용자 역할은 필수 입니다.")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
