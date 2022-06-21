package com.cgkim.myboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//TODO: domain, web 패키지로 분리. vo, dto
//TODO: 사용자가 쿠키 차단시 다른 방법을 찾아야

//TODO: 자유게시판 검색 지금 guest_nickname 만임. 멤버도 해야됨.
//TODO: question mapper select count 에서 불필요한 join 없애기

//TODO: 썸네일 이미지 원본 보여주기.
//TODO: 관리자 사이트 role?

//TODO: 관리자 사이트 완전 분리

//TODO: 게시물 수정시 thumbnailUri 컬럼 업데이트 해야함. 참고로 update 에 마이바티스 if 쓰는건 좋지 않다.
//TODO: sql 예약어는 대문자로. ddl, sql 전부 수정.
@SpringBootApplication
public class MyboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyboardApplication.class, args);
	}

}
