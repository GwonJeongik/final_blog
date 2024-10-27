package com.blog.domain.user.controller;

import com.blog.domain.user.dto.request.UserSignInRequest;
import com.blog.domain.user.dto.request.UserSignUpRequest;
import com.blog.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.blog.domain.user.entity.UserRole.ADMIN;
import static com.blog.domain.user.entity.UserRole.USER;
import static com.blog.util.jwt.JwtUtil.AUTHORIZATION_HEADER;
import static com.blog.util.jwt.JwtUtil.BEARER_PREFIX;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* 사용자 회원 가입 폼 */
    @GetMapping("/users/new")
    public String signUpUserForm(Model model) {
        log.info("signUpUserForm 호출");
        model.addAttribute("userSignUpRequest", new UserSignUpRequest());
        model.addAttribute("userRole", new ArrayList<>(List.of(USER, ADMIN)));
        return "users/createUser";

    }

    /* 사용자 회원 가입 */
    @PostMapping("/users/new")
    public String signUpUser(
            @Valid @ModelAttribute UserSignUpRequest requestDto,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "/") String redirectURL
    ) {
        log.info("signUpUser 호출");

        //1. 바인딩 된 오류가 있는지 검증 : 있으면 회원 가입 페이지로 이동 //
        if (bindingResult.hasErrors()) {
            log.info("signUpUser_bindingResult.hasErrors()={}", bindingResult.hasErrors());
            return "users/createUser";
        }

        //2. 사용자 회원 가입 진행 //
        userService.signUp(requestDto);

        //3. 사용자가 로그인 하기 전의 페이지로 리다이렉트 //
        return "redirect:" + redirectURL;
    }

    /* 사용자 로그인 폼 */
    @GetMapping("/users/login")
    public String signInUserForm(Model model) {
        log.info("signInUserForm 호출");
        model.addAttribute("userSignInRequest", new UserSignInRequest());
        return "users/login";
    }

    /* 사용자 로그인 */
    @PostMapping("/users/login")
    public String signInUser(
            @Valid @ModelAttribute UserSignInRequest signInRequest,
            BindingResult bindingResult,
            HttpServletResponse response,
            @RequestParam(defaultValue = "/") String redirectURL
    ) {
        log.info("signInUser 호출");

        //1. 바인딩 된 오류가 있는지 검증 : 있으면 회원 가입 페이지로 이동 //
        if (bindingResult.hasErrors()) {
            log.info("signInUser_bindingResult.hasErrors()={}", bindingResult.hasErrors());
            return "users/login";
        }

        //2. 비지니스 로직 호출 //
        String token = userService.signIn(signInRequest.getLoginEmail(), signInRequest.getPassword());

        //3. 헤더에 토큰 값 보관 //
        response.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + token);

        //3. 사용자가 로그인 하기 전의 페이지로 리다이렉트 //
        return "redirect:" + redirectURL;
    }

    /* 사용자 삭제 */
    @DeleteMapping("/{id}")
    public void deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
    }
}
