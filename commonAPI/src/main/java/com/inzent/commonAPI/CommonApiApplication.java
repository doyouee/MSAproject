package com.inzent.commonAPI;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
@Slf4j
public class CommonApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(CommonApiApplication.class, args);

	}

}
