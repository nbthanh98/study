package com.thanhnb.study.demo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/healCheck")
public class HealthCheckController {

    @Value("${clients.service-2.uri}")
    private String service2Uri;

    private final static RestTemplate restTemplate = new RestTemplate();
    private final static ObjectMapper objectMapper = new ObjectMapper();

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

    @RequestMapping(value = "/doSomeThing", method = RequestMethod.GET)
    public String doSomeThing() throws Exception {
        String uri = service2Uri + "/healCheck/getStatus1";
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        System.out.println("API: " + uri);
        System.out.println("Response: " + objectMapper.writeValueAsString(response));
        return response.getBody();
    }
}
