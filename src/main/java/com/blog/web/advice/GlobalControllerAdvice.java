package com.blog.web.advice;

import com.blog.domain.user.repository.UserRepository;
import com.blog.util.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import static com.blog.util.jwt.JwtUtil.*;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @ModelAttribute
    public void addUserToModel(
            HttpServletRequest request,
            Model model
    ) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);


    }
}
