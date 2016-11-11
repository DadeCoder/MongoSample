package com.dade.coreServer.controller;

import com.dade.common.utils.LogUtil;
import com.dade.coreServer.User.User;
import com.dade.coreServer.User.UserMongoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Dade on 2016/11/10.
 */
@RestController
@RequestMapping("/api/v1/coreServer")
public class UserController {

    @Autowired
    UserMongoDao userMongoDao;

    @RequestMapping("/test")
    String test(){
        String res = "Hello CoreServer!";
        return res;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    String add(@RequestBody User user){
        userMongoDao.insert(user);
        LogUtil.info(user);
        return "Hello MongoDB!";
    }

}
