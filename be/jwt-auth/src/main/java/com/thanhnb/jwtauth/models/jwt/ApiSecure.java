package com.thanhnb.jwtauth.models.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiSecure {

    RoleEnum[] requiredRoles();

    PrivilegeEnum[] requiredPrivileges();
}
