package com.dade.coreServer.basic;

import com.dade.common.mongodb.MongoObject;
import org.apache.log4j.Logger;

/**
 * Created by Dade on 2016/11/11.
 */
public abstract class BasicModelObject implements MongoObject {

    public abstract String getId();

    protected final static Logger log = Logger.getLogger(BasicModelObject.class.getName());

    public static final String FIELD_DELETED = "deleted";

}
