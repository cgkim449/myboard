package com.cgkim.myboardadmin.service.impl;

import com.cgkim.myboardadmin.dao.MemberDao;
import com.cgkim.myboardadmin.exception.LoginFailedException;
import com.cgkim.myboardadmin.exception.NicknameDuplicateException;
import com.cgkim.myboardadmin.exception.UsernameDuplicateException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.service.MemberService;
import com.cgkim.myboardadmin.util.SHA256PasswordEncoder;
import com.cgkim.myboardadmin.vo.member.MemberVo;
import com.cgkim.myboardadmin.vo.member.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;
    private final SHA256PasswordEncoder sha256PasswordEncoder;

    /**
     * 회원가입
     */
    @Override
    public String signUp(SignUpRequest signUpRequest) throws NoSuchAlgorithmException {
        if(memberDao.selectCountByUsername(signUpRequest.getUsername()) > 0) {
            throw new UsernameDuplicateException(ErrorCode.USERNAME_DUPLICATE);
        }

        if(memberDao.selectCountByNickname(signUpRequest.getNickname()) > 0) {
            throw new NicknameDuplicateException(ErrorCode.NICKNAME_DUPLICATE);
        }

        String encodedPassword = sha256PasswordEncoder.getHash(signUpRequest.getPassword());
        signUpRequest.setPassword(encodedPassword);

        memberDao.insert(signUpRequest);
        return signUpRequest.getUsername();
    }

    /**
     * 로그인
     *
     * @return null: 로그인 실패
     */
    @Override
    public MemberVo login(String username, String password) throws NoSuchAlgorithmException {
        MemberVo memberVo = memberDao.selectByUsername(username);

        if(memberVo == null) { //일치하는 아이디가 없을때
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        password = sha256PasswordEncoder.getHash(password);
        if(!memberVo.getPassword().equals(password)) { //비밀번호가 틀렸을 때
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        return memberVo;
    }

    /**
     * 회원 상세 정보
     */
    @Override
    public MemberVo getMemberDetail(String username) {
        return memberDao.selectByUsername(username);
    }

    /**
     * username 으로 memberId 조회
     */
    @Override
    public Long getMemberId(String username) {
        return memberDao.selectMemberIdByUsername(username);
    }
}
