package com.yoon.joinproject.mapper;

import com.yoon.joinproject.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Mapper
public interface UserMapper {
    int saveUser(UserVo userVo);

    String searchUser(UserVo userVo);

    Map<String, Object> loginCheck(UserVo userVo);

    Map<String, Object> pwdCheck(UserVo userVo);

    int updPwd(UserVo userVo);
}
