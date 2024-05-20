package com.projectA1.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.projectA1.config.auth.PrincipalUser;

@ControllerAdvice
public class GlobalControllerAdvice {

	
	//인증
    @ModelAttribute("authentication")
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
 // 권한
    @ModelAttribute("roles")
    public List<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = new ArrayList<>();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            Collection<? extends GrantedAuthority> authorities = ((UserDetails) authentication.getPrincipal()).getAuthorities();
            for (GrantedAuthority authority : authorities) {
                roles.add(authority.getAuthority());
            }
        }
        return roles;
    }
    
    @ModelAttribute("userId")
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            PrincipalUser user = (PrincipalUser) authentication.getPrincipal();
            return user.getUserId(); // UserDetails 인터페이스의 getUsername()를 통해 사용자 ID 반환
        }
        return null;
    }

}
