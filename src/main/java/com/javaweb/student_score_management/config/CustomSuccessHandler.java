package com.javaweb.student_score_management.config;

import com.javaweb.student_score_management.service.implement.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String redirectUrl;

        switch (userDetails.getRole()) {
            case Admin:
                redirectUrl = "/admin/index";
                break;
            case GiangVien:
                redirectUrl = "/giangvien/index";
                break;
            case SinhVien:
                redirectUrl = "/sinhvien/index";
                break;
            default:
                redirectUrl = "/login";
        }

        response.sendRedirect(redirectUrl);
    }
}