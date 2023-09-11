package com.thanhnb.jwtauth.controllers;

import com.thanhnb.jwtauth.models.Allowed;
import com.thanhnb.jwtauth.models.PrivilegeEnum;
import com.thanhnb.jwtauth.models.RoleEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/protected")
public class DemoControllers {

    @Allowed(roles = RoleEnum.ADMIN, privileges = PrivilegeEnum.READ)
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "test";
    }
}
