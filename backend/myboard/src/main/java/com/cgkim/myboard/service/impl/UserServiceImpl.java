package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.UserDao;
import com.cgkim.myboard.exception.DuplicateNicknameException;
import com.cgkim.myboard.exception.DuplicateUsernameException;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.service.UserService;
import com.cgkim.myboard.vo.user.SignUpRequest;
import com.cgkim.myboard.vo.user.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    //TODO: 비밀번호 암호화
    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if(userDao.selectCountByUsername(signUpRequest.getUsername()) > 0) {
            throw new DuplicateUsernameException(ErrorCode.DUPLICATE_USERNAME);
        }

        if(userDao.selectCountByNickname(signUpRequest.getNickname()) > 0) {
            throw new DuplicateNicknameException(ErrorCode.DUPLICATE_NICKNAME);
        }

        userDao.insert(signUpRequest);

        return SignUpResponse.builder()
                .userId(signUpRequest.getUserId())
                .username(signUpRequest.getUsername())
                .nickname(signUpRequest.getNickname())
                .build();
    }
}
