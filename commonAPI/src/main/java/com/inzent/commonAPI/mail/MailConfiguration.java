package com.inzent.commonAPI.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * 메일 설정
 * @author 조유진
 */

@Configuration
public class MailConfiguration {
	
	@Value("${mail.protocol}")
	private String mailProtocol;
	@Value("${mail.host}")
	private String mailHost;
	@Value("${mail.port}")
	private String mailPort;
	@Value("${mail.username}")
	private String mailUserName;
	@Value("${mail.password}")
	private String mailPassword;
	
	@Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setProtocol(mailProtocol);
        javaMailSender.setHost(mailHost);
        javaMailSender.setPort(Integer.parseInt(mailPort));
        javaMailSender.setUsername(mailUserName);
        javaMailSender.setPassword(mailPassword);

        return javaMailSender;
    }
}
