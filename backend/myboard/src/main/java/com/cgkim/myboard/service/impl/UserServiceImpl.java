package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.UserDao;
import com.cgkim.myboard.exception.DuplicateNicknameException;
import com.cgkim.myboard.exception.DuplicateUsernameException;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.LoginFailedException;
import com.cgkim.myboard.service.UserService;
import com.cgkim.myboard.vo.user.SignUpRequest;
import com.cgkim.myboard.vo.user.SignUpResponse;
import com.cgkim.myboard.vo.user.UserVo;
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

    /**
     * 로그인
     *
     * @param username
     * @param password
     * @return null: 로그인 실패
     */
    @Override
    public UserVo login(String username, String password) {
        UserVo userVo = userDao.selectByUsername(username);

        if(userVo == null) { // 일치하는 username 이 없을때
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        if(!userVo.getPassword().equals(password)) { // 비밀번호가 틀렸을 때
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        return userVo;
    }

    @Override
    public UserVo getUserDetail(String username) {
        return userDao.selectByUsername(username);
    }

    @Override
    public Long getUserId(String username) {
        return userDao.selectUserIdByUsername(username);
    }
}
