package com.heima.kafka.listener;

import com.alibaba.fastjson.JSON;
import org.apache.catalina.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HelloListener {
    @KafkaListener(topics = "first")
    public void listen(String message){
        if(!StringUtils.isEmpty(message)){
            User user = JSON.parseObject(message, User.class);
            System.out.println("接收到消息："+user);
        }
    }
}
