package com.dade.coreServer.User;

import com.dade.common.mongodb.BasicMongoDao;
import org.springframework.stereotype.Component;

/**
 * Created by Dade on 2016/11/11.
 */
@Component
public class UserMongoDao extends BasicMongoDao<User> {
    @Override
    public Class<User> getReturnClass() {
        return User.class;
    }
}
