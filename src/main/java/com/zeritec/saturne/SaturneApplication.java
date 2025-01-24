package com.zeritec.saturne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.zeritec.saturne.configuration.RsaKeyProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class SaturneApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaturneApplication.class, args);
	}

}
