package com.j4.krevetka.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@PropertySource("application.properties")
public class Config {

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String token;

    public String getBotName(){
        return botName;
    }

    public String getToken(){
        return token;
    }
}
