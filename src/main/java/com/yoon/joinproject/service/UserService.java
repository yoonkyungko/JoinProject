package com.yoon.joinproject.service;

import com.yoon.joinproject.mapper.UserMapper;
import com.yoon.joinproject.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Transactional
    public int joinUser(UserVo userVo){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userVo.setUserPwd(passwordEncoder.encode(userVo.getUserPwd()));

        return userMapper.saveUser(userVo);
    }

    @Transactional
    public String searchUser(UserVo userVo){
        return userMapper.searchUser(userVo);
    }

    @Transactional
    public Map<String, Object> loginCheck(UserVo userVo){
        return userMapper.loginCheck(userVo);
    }

    @Transactional
    public Map<String, Object> pwdCheck(UserVo userVo){
        return userMapper.pwdCheck(userVo);
    }

    @Transactional
    public int updPwd(UserVo userVo){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userVo.setUserPwd(passwordEncoder.encode(userVo.getUserPwd()));

        return userMapper.updPwd(userVo);
    }
}
