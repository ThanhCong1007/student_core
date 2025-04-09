package com.javaweb.student_score_management.service.implement;

import com.javaweb.student_score_management.entity.TaiKhoanEntity;
import com.javaweb.student_score_management.service.intface.AccountRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CustomUserDetailsSerImplement implements UserDetailsService {
    private final AccountRepository accountRepository;

    public CustomUserDetailsSerImplement(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("DEBUG: Đang load user với username: " + username);

        TaiKhoanEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("DEBUG: Không tìm thấy user!");
                    return new UsernameNotFoundException("Account not found");
                });

        System.out.println("DEBUG: Tìm thấy user, role: " + account.getRole().name());

        return new CustomUserDetails(account);
    }
}