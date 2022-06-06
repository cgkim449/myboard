package com.cgkim.myboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//TODO: domain, web 패키지로 분리. vo, dto
//TODO: 썸네일 이미지 원본 보여주기.

//TODO: 걍 쿠키로 내려주기 그럼 프론트에서 쿠키에 저장할 필요가 없다. 그러나 쿠키 차단시의 방법을 찾아야.
// - 사용자가 쿠키 차단시 현재 로그인 방법 못씀. 다른 방법 찾아야.
//TODO: db 이름 member로 바꾸기,
// (기본적인 방법)컨트롤러 메서드 둘로 나누기, aop 사용해서 비밀번호 체크하기, aop 사용해서 로그인시 userid 가져오기(아무튼 argument resolver에서 service 사용하지 말기)
// jwt 에서 username 추출하는걸 argument resolve에 두는건 안그럼 aop에서 검증을 한번더 해야하니까
@SpringBootApplication
public class MyboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyboardApplication.class, args);
	}

}
