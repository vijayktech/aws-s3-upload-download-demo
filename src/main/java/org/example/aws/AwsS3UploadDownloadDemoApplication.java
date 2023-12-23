package org.example.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AwsS3UploadDownloadDemoApplication {

	public static void main(String[] args) {
		System.out.println("App is up");
		SpringApplication.run(AwsS3UploadDownloadDemoApplication.class, args);
	}

}
