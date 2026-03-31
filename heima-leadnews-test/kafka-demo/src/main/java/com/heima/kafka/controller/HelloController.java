package com.heima.kafka.controller;

import com.alibaba.fastjson.JSON;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;

@RestController
public class HelloController {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/hello")
    public String hello(){
        User user = new User();
        user.setUsername("heima");
        kafkaTemplate.send("first", JSON.toJSONString(user));
        return "success";

    }


}
