package com.xiaohe66.crud.demo.service;

import com.xiaohe66.common.dto.Page;
import com.xiaohe66.crud.demo.entity.User;
import com.xiaohe66.crud.server.ICrudService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * @author xiaohe
 * @since 2021.10.18 17:13
 */
@Slf4j
//@Service
public class UserCrudService implements ICrudService<User> {

    @Override
    public boolean add(User params) {
        log.info("我的实现：增");
        return true;
    }

    @Override
    public boolean modifyById(User params) {
        log.info("我的实现：改");
        return true;
    }

    @Override
    public Page<User> listByParam(User params, int pageNo, int pageSize) {
        log.info("我的实现：查");

        User user = new User();
        user.setId(1L);
        user.setName("1111");

        return Page.<User>builder()
                .size(15)
                .current(1)
                .pages(1)
                .records(Collections.singletonList(user))
                .build();
    }

    @Override
    public User getById(Long id) {
        log.info("我的实现：查");


        User user = new User();
        user.setId(2L);
        user.setName("2222");

        return user;
    }

    @Override
    public boolean removeById(Long id) {
        log.info("我的实现：删");
        return true;
    }

    @Override
    public Class<User> getEntityCls() {
        return User.class;
    }
}
