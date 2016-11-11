package com.dade.coreServer.User;

import com.dade.common.mongodb.BasicMongoDao;
import com.dade.coreServer.basic.BasicModelObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Dade on 2016/11/11.
 */
@Document(collection = "user")
public class User extends BasicModelObject {

    @Id
    String id;

    String name;
    Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
