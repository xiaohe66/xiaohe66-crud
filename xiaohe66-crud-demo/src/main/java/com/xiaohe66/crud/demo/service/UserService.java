package com.xiaohe66.crud.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohe66.crud.demo.entity.User;
import com.xiaohe66.crud.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author xiaohe
 * @since 2021.09.26 11:51
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
