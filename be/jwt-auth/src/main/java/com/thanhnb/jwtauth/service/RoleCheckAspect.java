package com.thanhnb.jwtauth.service;

import com.thanhnb.jwtauth.exception.CustomException;
import com.thanhnb.jwtauth.models.jwt.*;
import com.thanhnb.jwtauth.repository.jwt.PrivilegeRepository;
import com.thanhnb.jwtauth.repository.jwt.RoleRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class RoleCheckAspect {

    @Autowired private RoleRepository roleRepository;
    @Autowired private PrivilegeRepository privilegeRepository;

    @Before("@annotation(com.thanhnb.jwtauth.models.jwt.ApiSecure)")
    public void before(JoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        RoleEnum[] expectedRoles = ms.getMethod().getAnnotation(ApiSecure.class).requiredRoles();
        PrivilegeEnum[] expectedPrivileges = ms.getMethod().getAnnotation(ApiSecure.class).requiredPrivileges();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> userRoles = roleRepository.loadRolesOfUserBy(userDetails.getUsername());
        if (Objects.isNull(userRoles) || userRoles.isEmpty()) {
            throw new CustomException("Bạn không có quyền truy cập API");
        }
        // 1. Check match role
        boolean isMatchRole = isMatchRole(userRoles, expectedRoles);
        if (!isMatchRole) {
            throw new CustomException("Bạn không có quyền truy cập API");
        }
        // 2. Check match privileges
        List<Privileges> userPrivileges = new ArrayList<>();
        for (Role role: userRoles) {
            userPrivileges.addAll(privilegeRepository.loadPrivilegesBy(role.getCode()));
        }
        boolean isMatchPrivileges = isMatchPrivileges(userPrivileges, expectedPrivileges);
        if (!isMatchPrivileges) {
            throw new CustomException("Bạn không có quyền truy cập API");
        }
    }

    private boolean isMatchRole(List<Role> userRoles, RoleEnum[] expectedRoles) {
        for (Role role: userRoles) {
            for (RoleEnum roleExpected : expectedRoles) {
                if (role.getCode().equalsIgnoreCase(roleExpected.name())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMatchPrivileges(List<Privileges> userPrivileges, PrivilegeEnum[] expectedPrivileges) {
        for (Privileges privilege: userPrivileges) {
            for (PrivilegeEnum expectedPrivilege: expectedPrivileges) {
                if (privilege.getCode().equalsIgnoreCase(expectedPrivilege.name())) {
                    return true;
                }
            }
        }
        return false;
    }

}
