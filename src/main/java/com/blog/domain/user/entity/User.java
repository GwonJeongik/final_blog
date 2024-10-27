package com.blog.domain.user.entity;

import com.blog.domain.common.auditing.BaseTimeEntity;
import com.blog.domain.user.dto.request.UserSignUpRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.blog.domain.user.entity.UserStatus.ACTIVE;
import static com.blog.domain.user.entity.UserStatus.DELETE;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loginEmail;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /* 사용자 생성 */
    public static User createUser(UserSignUpRequest requestDto) {
        User user = new User();
        user.name = requestDto.getName();
        user.loginEmail = requestDto.getLoginEmail();
        user.password = requestDto.getPassword();
        user.role = requestDto.getRole();
        user.status = ACTIVE;

        return user;
    }

    /* 회원 삭제 : 상태 삭제로 변경 */
    public void deleteUser() {
        this.status = DELETE;
    }
}
