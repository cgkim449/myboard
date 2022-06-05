package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.UserDao;
import com.cgkim.myboard.exception.NicknameDuplicateException;
import com.cgkim.myboard.exception.UsernameDuplicateException;
import com.cgkim.myboard.exception.ErrorCode;
import com.cgkim.myboard.exception.LoginFailedException;
import com.cgkim.myboard.service.UserService;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.user.SignUpRequest;
import com.cgkim.myboard.vo.user.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final SHA256PasswordEncoder sha256PasswordEncoder;

    /**
     * 회원가입
     */
    @Override
    public String signUp(SignUpRequest signUpRequest) throws NoSuchAlgorithmException {
        if(userDao.selectCountByUsername(signUpRequest.getUsername()) > 0) {
            throw new UsernameDuplicateException(ErrorCode.USERNAME_DUPLICATE);
        }

        if(userDao.selectCountByNickname(signUpRequest.getNickname()) > 0) {
            throw new NicknameDuplicateException(ErrorCode.NICKNAME_DUPLICATE);
        }

        String encodedPassword = sha256PasswordEncoder.getHash(signUpRequest.getPassword());
        signUpRequest.setPassword(encodedPassword);

        userDao.insert(signUpRequest);
        return signUpRequest.getUsername();
    }

    /**
     * 로그인
     * @return null: 로그인 실패
     */
    @Override
    public UserVo login(String username, String password) throws NoSuchAlgorithmException {
        UserVo userVo = userDao.selectByUsername(username);

        if(userVo == null) { //일치하는 아이디가 없을때
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        password = sha256PasswordEncoder.getHash(password);
        if(!userVo.getPassword().equals(password)) { //비밀번호가 틀렸을 때
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        return userVo;
    }

    /**
     * 회원 상세 정보
     */
    @Override
    public UserVo getUserDetail(String username) {
        return userDao.selectByUsername(username);
    }

    /**
     * username 으로 userId 조회
     */
    @Override
    public Long getUserId(String username) {
        return userDao.selectUserIdByUsername(username);
    }
}
