package com.thanhnb.study.demo.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config-map")
public class ConfigMapController {

    @Value("${greeter.message}")
    private String message;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String configMap() {
        return "configMapValue: " + this.message;
    }
}
