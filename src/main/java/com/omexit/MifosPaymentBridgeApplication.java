package com.omexit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MifosPaymentBridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MifosPaymentBridgeApplication.class, args);
	}
}
