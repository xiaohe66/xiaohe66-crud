package com.xiaohe66.crud.demo.controller;

import com.xiaohe66.common.dto.R;
import com.xiaohe66.crud.demo.entity.User;
import com.xiaohe66.crud.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaohe
 * @since 2021.09.26 11:54
 */
@RestController
@RequestMapping("/xiaohe66/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping("/test")
    public R<User> request() {

        return R.ok(userService.getById(1));
        //return R.ok(null);

    }


}
