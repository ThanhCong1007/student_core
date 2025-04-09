package com.javaweb.student_score_management.service.implement;

import com.javaweb.student_score_management.entity.TaiKhoanEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final TaiKhoanEntity taiKhoan;

    public CustomUserDetails(TaiKhoanEntity taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + taiKhoan.getRole().name().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return taiKhoan.getPassword();
    }

    @Override
    public String getUsername() {
        return taiKhoan.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getMaTK() {
        return taiKhoan.getMaTK();
    }

    public TaiKhoanEntity.Role getRole() {
        return taiKhoan.getRole();
    }

    public Integer getMaGV() {
        return taiKhoan.getMaGV() != null ? taiKhoan.getMaGV().getMaGV() : null;
    }

    public String getTenGV() {
        return taiKhoan.getMaGV() != null ? taiKhoan.getMaGV().getTenGV() : "Unknown";
    }

    public String getEmail() {
        return taiKhoan.getMaGV() != null ? taiKhoan.getMaGV().getEmail() : "Unknown";
    }

    public Integer getMaSV() {
        return taiKhoan.getMaSV() != null ? taiKhoan.getMaSV().getMaSV() : null;
    }

    public String getTenSV() {
        return taiKhoan.getMaSV() != null ? taiKhoan.getMaSV().getTenSV() : "Unknown";
    }
}