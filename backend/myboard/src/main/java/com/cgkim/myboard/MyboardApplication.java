package com.cgkim.myboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//TODO: domain, web 패키지로 분리. vo, dto
//TODO: 썸네일 이미지 원본 보여주기.
@SpringBootApplication
public class MyboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyboardApplication.class, args);
	}

}
