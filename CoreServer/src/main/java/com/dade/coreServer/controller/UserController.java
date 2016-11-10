package com.dade.coreServer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dade on 2016/11/10.
 */
@RestController
@RequestMapping("/api/v1/coreServer")
public class UserController {

    @RequestMapping("/test")
    String test(){
        String res = "Hello CoreServer!";
        return res;
    }

}
