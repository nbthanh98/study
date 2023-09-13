package com.thanhnb.jwtauth.controllers;

import com.thanhnb.jwtauth.models.ApiSecure;
import com.thanhnb.jwtauth.models.PrivilegeEnum;
import com.thanhnb.jwtauth.models.RoleEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/protected")
public class DemoControllers {

    @ApiSecure(requiredRoles = RoleEnum.ADMIN, requiredPrivileges = {PrivilegeEnum.READ, PrivilegeEnum.VIEW})
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "test";
    }
}
