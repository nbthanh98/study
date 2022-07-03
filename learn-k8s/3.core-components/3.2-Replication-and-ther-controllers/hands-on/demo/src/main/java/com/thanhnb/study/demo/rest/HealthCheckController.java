package com.thanhnb.study.demo.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/healCheck")
public class HealthCheckController {

    @RequestMapping(value = "/getStatus1", method = RequestMethod.GET)
    public String getStatus() {
        System.out.println("/healCheck " + System.currentTimeMillis());
        return "Service Running";
    }

    @RequestMapping(value = "/getStatus2", method = RequestMethod.GET)
    public String getStatus2() throws Exception {
        System.out.println("/healCheck " + System.currentTimeMillis());
        throw new Exception();
    }
}
