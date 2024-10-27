package com.blog.domain.user.service;

import com.blog.domain.common.exception.user.DuplicateLoginEmailException;
import com.blog.domain.common.exception.user.InvalidPassword;
import com.blog.domain.common.exception.user.NotFoundUserException;
import com.blog.domain.user.dto.request.UserSignUpRequest;
import com.blog.domain.user.entity.User;
import com.blog.domain.user.repository.UserRepository;
import com.blog.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /* 사용자 회원 가입 */
    public void signUp(UserSignUpRequest requestDto) {
        //1. 사용자 로그인 이메일 중복 확인 //
        isDuplicateLoginEmail(requestDto);

        //2. 비밀번호 인코딩 //
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        requestDto.encodePassword(encodedPassword);

        //3. 사용자 엔티티 생성 //
        User user = User.createUser(requestDto);

        //4. 사용자 엔티티 저장 //
        userRepository.save(user);
    }

    /* 사용자 로그인 */
    public String signIn(String loginEmail, String password) {
        //1. 회원 로그인 이메일 검증 //
        User findUser = userRepository
                .findUserByLoginEmail(loginEmail)
                .orElseThrow(NotFoundUserException::new);

        //2. 회원 비밀번호 검증 //
        isInvalidPassword(password, findUser);

        //3. 토큰 생성 //
        return jwtUtil.createToken(findUser.getId(), findUser.getRole());
    }

    /* 사용자 삭제 */
    public void delete(Long id) {
        //1. 회원 조회 //
        User user = userRepository.findById(id).orElseThrow(NotFoundUserException::new);

        //2. 회원 상태 삭제로 변경 //
        user.deleteUser();
    }

    /* 중복된 이메일 검증 메서드 */
    private void isDuplicateLoginEmail(UserSignUpRequest requestDto) {
        if (userRepository.findUserByLoginEmail(requestDto.getLoginEmail()).isPresent()) {
            throw new DuplicateLoginEmailException();
        }
    }

    /* 비밀번호 검증 메서드 */
    private void isInvalidPassword(String password, User findUser) {
        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            throw new InvalidPassword();
        }
    }

    /* 사용자 조회 */
    public boolean findOne(Long id) {
        return userRepository.findById(id).isPresent();
    }
}
