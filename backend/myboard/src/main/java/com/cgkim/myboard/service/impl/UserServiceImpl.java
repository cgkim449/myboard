package com.cgkim.myboard.service.impl;

import com.cgkim.myboard.dao.UserDao;
import com.cgkim.myboard.service.UserService;
import com.cgkim.myboard.vo.User.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public void signUp(UserVo userVo) {
        userDao.insert(userVo);
    }
}
