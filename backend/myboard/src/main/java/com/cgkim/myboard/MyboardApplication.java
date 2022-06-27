package com.cgkim.myboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//TODO: domain, web 패키지로 분리. vo, dto

//TODO: 자유게시판 검색 지금 guest_nickname 만임. 멤버도 해야됨.
//TODO: question mapper select count 에서 불필요한 join 없애기

//TODO: 참고로 update 에 마이바티스 if 쓰는건 좋지 않다.
//TODO: sql 예약어는 대문자로. ddl, sql 전부 수정.

//TODO: vo, dto 명확히 구분. Request 는 날라오는 값만 필드로 가지게. Response 는 필드에 null 안들어가게?
@SpringBootApplication
public class MyboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyboardApplication.class, args);
	}

}
