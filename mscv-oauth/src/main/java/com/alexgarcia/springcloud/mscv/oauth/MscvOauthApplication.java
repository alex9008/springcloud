package com.alexgarcia.springcloud.mscv.oauth;

 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MscvOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(MscvOauthApplication.class, args);
	}

}
