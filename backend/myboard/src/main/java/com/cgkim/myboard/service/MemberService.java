package com.cgkim.myboard.service;

import com.cgkim.myboard.dao.MemberDao;
import com.cgkim.myboard.exception.MemberNotFoundException;
import com.cgkim.myboard.exception.NicknameDuplicateException;
import com.cgkim.myboard.exception.UsernameDuplicateException;
import com.cgkim.myboard.exception.errorcode.ErrorCode;
import com.cgkim.myboard.exception.LoginFailedException;
import com.cgkim.myboard.util.SHA256PasswordEncoder;
import com.cgkim.myboard.vo.member.MemberVo;
import com.cgkim.myboard.vo.member.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * 회원 Service
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;

    private final SHA256PasswordEncoder sha256PasswordEncoder;

    /**
     * 회원가입
     *
     * @param signUpRequest
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public String signUp(SignUpRequest signUpRequest) throws NoSuchAlgorithmException {

        if (isUsernameDuplicated(signUpRequest.getUsername())) {
            throw new UsernameDuplicateException(ErrorCode.USERNAME_DUPLICATE);
        }

        if (isNicknameDuplicated(signUpRequest.getNickname())) {
            throw new NicknameDuplicateException(ErrorCode.NICKNAME_DUPLICATE);
        }

        String encodedPassword = sha256PasswordEncoder.getHash(signUpRequest.getPassword());

        signUpRequest.setPassword(encodedPassword);
        memberDao.insert(signUpRequest);

        return signUpRequest.getUsername();
    }

    private boolean isNicknameDuplicated(String nickname) {

        return memberDao.selectCountByNickname(nickname) > 0;
    }

    private boolean isUsernameDuplicated(String username) {

        return memberDao.selectCountByUsername(username) > 0;
    }

    /**
     * 로그인
     *
     * @param username
     * @param password
     * @return MemberVo
     * @throws NoSuchAlgorithmException
     */
    public MemberVo login(String username, String password) throws NoSuchAlgorithmException {

        MemberVo memberVo = memberDao.selectByUsername(username);

        if (isUsernameNotCorrect(memberVo)) {
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        password = sha256PasswordEncoder.getHash(password);

        if (isPasswordNotCorrect(password, memberVo)) {
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        return memberVo;
    }

    private boolean isPasswordNotCorrect(String password, MemberVo memberVo) {
        return !memberVo.getPassword().equals(password);
    }

    private boolean isUsernameNotCorrect(MemberVo memberVo) {
        return memberVo == null;
    }

    /**
     * username 으로 memberId 조회
     *
     * @param username
     * @return Long
     */
    public Long getMemberIdBy(String username) {
        Long memberId = memberDao.selectMemberIdByUsername(username);

        if (memberId == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return memberId;
    }
}
