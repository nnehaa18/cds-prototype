package com.backup.central_data_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"com.central_data_storage.backup", "com.central_data_storage.s3"})
public class CentralDataStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralDataStorageApplication.class, args);
	}

}
