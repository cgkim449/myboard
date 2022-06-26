package com.cgkim.myboardadmin.service;

import com.cgkim.myboardadmin.dao.AdminDao;
import com.cgkim.myboardadmin.exception.LoginFailedException;
import com.cgkim.myboardadmin.exception.errorcode.ErrorCode;
import com.cgkim.myboardadmin.util.SHA256PasswordEncoder;
import com.cgkim.myboardadmin.vo.admin.AdminVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * 관리자 Service
 */
@Service
@RequiredArgsConstructor
public class AdminService{

    private final AdminDao adminDao;

    private final SHA256PasswordEncoder sha256PasswordEncoder;

    /**
     * 로그인
     *
     * @param username
     * @param password
     * @return AdminVo
     * @throws NoSuchAlgorithmException
     */
    public AdminVo login(String username, String password) throws NoSuchAlgorithmException {

        AdminVo adminVo = adminDao.selectByUsername(username);

        if(isUsernameNotCorrect(adminVo)) {
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        password = sha256PasswordEncoder.getHash(password);

        if(isPasswordNotCorrect(password, adminVo)) { //비밀번호가 틀렸을 때
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        return adminVo;
    }

    private boolean isUsernameNotCorrect(AdminVo adminVo) {
        return adminVo == null;
    }

    private boolean isPasswordNotCorrect(String password, AdminVo adminVo) {
        return !adminVo.getPassword().equals(password);
    }
}
