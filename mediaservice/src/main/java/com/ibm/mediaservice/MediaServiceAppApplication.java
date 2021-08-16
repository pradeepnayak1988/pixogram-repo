package com.ibm.mediaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MediaServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaServiceAppApplication.class, args);
	}

}
