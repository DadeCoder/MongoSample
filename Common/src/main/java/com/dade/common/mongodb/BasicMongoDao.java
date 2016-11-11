package com.dade.common.mongodb;

import com.dade.common.utils.LogUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

/**
 * Created by Dade on 2016/11/11.
 */
public abstract class BasicMongoDao< T extends MongoObject> {

    private static final String FIELD_DELETED = "deleted";

    protected  static final String FIELD_CREATE_DATE = "createDate";
    protected  static final String FIELD_MODIFY_DATE = "modifyDate";

    @Autowired
    protected MongoOperations mongoOperations;

    public boolean hasDeletedField() {
        return false;
    }

    abstract public Class< T > getReturnClass();

    protected String createObjectId() {
        return new ObjectId().toString();
    }

    public T findById(String id) {
        return mongoOperations.findById(id, getReturnClass());
    }

    public T insert(T obj)
    {
        LogUtil.info("insert= " + obj.toString() + ", class= " + getClass().getSimpleName());

        mongoOperations.insert(obj);
        return obj;
    }

    public void insertAll(List< T > list)
    {
        LogUtil.info("insertAll-list= " + list + ", class= " + getClass().getSimpleName());

        mongoOperations.insertAll(list);
    }

    public T remove(String id)
    {
        LogUtil.info("remove= " + id + ", class= " + getClass().getSimpleName());

        Query query = Query.query(Criteria.where("id").is(id));
        return mongoOperations.findAndRemove(query, getReturnClass());
    }

    public T remove(Collection< String > ids)
    {
        LogUtil.info("remove= " + ids + ", class= " + getClass().getSimpleName());

        Query query = Query.query(Criteria.where("id").in(ids));
        return mongoOperations.findAndRemove(query, getReturnClass());
    }

    public T executeDelete(String id)
    {
        LogUtil.info("executeDelete= " + id + ", class= " + getClass().getSimpleName());

        if ( hasDeletedField() ) {
            LogUtil.info("delete by update id=" + id + ", class= " + getClass().getSimpleName());
            return findAndModify(id, FIELD_DELETED, true);
        }
        else {
            return remove(id);
        }
    }

    public void updateFirst(String id, T objectToUpdate)
    {
        Query query = Query.query(Criteria.where("id").is(id));
        DBObject dbObject = (DBObject) mongoOperations.getConverter().convertToMongoType(objectToUpdate);
        Update setUpdate = Update.fromDBObject(new BasicDBObject("$set", dbObject));

        LogUtil.info("updateFirst query=" + query.toString() +
                ", update= " + setUpdate.toString() +
                ", class= " + getClass().getSimpleName());

        mongoOperations.updateFirst(query, setUpdate, getReturnClass());
    }

    public T findAndModify(String id, T objectToUpdate)
    {
        Query query = Query.query(Criteria.where("id").is(id));
        DBObject dbObject = (DBObject) mongoOperations.getConverter().convertToMongoType(objectToUpdate);
        dbObject.removeField("_id");
        Update setUpdate = Update.fromDBObject(new BasicDBObject("$set", dbObject));

        LogUtil.info("findAndModify query=" + query.toString() +
                ", update= " + setUpdate.toString() +
                ", class= " + getClass().getSimpleName());

        return mongoOperations.findAndModify(query, setUpdate, new FindAndModifyOptions().returnNew(true), getReturnClass());
    }


    public T findAndModify(String id, String key, Object value) {
        return mongoOperations.findAndModify(
                Query.query(Criteria.where("id").is(id)),
                Update.update(key, value),
                new FindAndModifyOptions().returnNew(true),
                getReturnClass());
    }


    public void updateFirst(String id, String key, Object value) {
        mongoOperations.updateFirst(
                Query.query(Criteria.where("id").is(id)),
                Update.update(key, value),
                getReturnClass());
    }


    public List< T > findAll() {

        List< T > list = null;
        Query query;
        if ( hasDeletedField() ) {
            query = Query.query(Criteria.where(FIELD_DELETED).ne(true));
        }
        else {
            query = new Query();
        }

        LogUtil.info("findAll query=" + query.toString() +
                ", class= " + getClass().getSimpleName());

        list = mongoOperations.find(query, getReturnClass());
        if ( list == null ) {
            return Collections.emptyList();
        }
        return list;
    }


    public T findOne(String key, String value) {
        Query query;
        if ( hasDeletedField() ) {
            query = Query.query(Criteria.where(key).is(value).and(FIELD_DELETED).ne(true));
        }
        else {
            query = Query.query(Criteria.where(key).is(value));
        }

        LogUtil.info("findOne query=" + query.toString() + ", class= " + getClass().getSimpleName());
        return mongoOperations.findOne(query, getReturnClass());
    }

    public T findOne(String key, int value) {

        Query query;
        if ( hasDeletedField() ) {
            query = Query.query(Criteria.where(key).is(value).and(FIELD_DELETED).ne(true));
        }
        else {
            query = Query.query(Criteria.where(key).is(value));
        }

        LogUtil.info("findOne query=" + query.toString() + ", class= " + getClass().getSimpleName());
        return mongoOperations.findOne(query, getReturnClass());
    }

    public List< T > find(Query query)
    {
        LogUtil.info("find query=" + query.toString() +
                ", skip= " + query.getSkip() +
                ", limit= " + query.getLimit() +
                ", class= " + getClass().getSimpleName());

        List< T > list = mongoOperations.find(query, getReturnClass());
        if ( list == null ) {
            return Collections.emptyList();
        }
        return list;
    }

    public long count (Query query)
    {
        return mongoOperations.count(query, getReturnClass());
    }

    public List< T > findInValues(String field, Collection< ? > values) {
        if ( values == null || values.size() == 0 ) {
            return Collections.emptyList();
        }

        Criteria criteria = Criteria.where(field).in(values);
        if ( hasDeletedField() && !criteria.getCriteriaObject().containsField(FIELD_DELETED) ) {
            criteria.and(FIELD_DELETED).ne(true);
        }

        Query query = Query.query(criteria);
        LogUtil.info("findInValues query= " + query.toString() + ", class= " + getClass().getSimpleName());
        List< T > list = mongoOperations.find(query, getReturnClass());
        if ( list == null ) {
            return Collections.emptyList();
        }
        return list;
    }

    public List< String > toIdList(Collection< ? extends MongoObject > objList) {
        if ( objList == null || objList.size() == 0 ) {
            return Collections.emptyList();
        }

        List< String > idList = new ArrayList< String >();
        for ( MongoObject t : objList ) {
            idList.add(t.getId());
        }

        return idList;
    }

    public List< T > findByObjectList(Collection< ? extends MongoObject > objList) {
        return findByIdList(toIdList(objList));
    }

    public List< T > findByIdList(Collection< String > idList) {
        if ( idList == null || idList.size() == 0 ) {
            LogUtil.warn(getReturnClass().getSimpleName() + " empty id list");
            return Collections.emptyList();
        }

        Criteria criteria = Criteria.where("id").in(idList);
        if ( hasDeletedField() ) {
            criteria.and(FIELD_DELETED).ne(true);
        }

        List< T > list = mongoOperations.find(Query.query(criteria), getReturnClass());
        if ( list == null || list.size() == 0 ) {
            LogUtil.warn(getReturnClass().getSimpleName() + " idList is  " + idList.toString() + ", no data found");
            return Collections.emptyList();
        }

        HashMap< String, T > map = new HashMap< String, T >();
        for ( T t : list ) {
            map.put(t.getId(), t);
        }

        ArrayList< T > retList = new ArrayList< T >();
        for ( String id : idList ) {
            if ( map.containsKey(id) ) {
                T t = map.get(id);
                retList.add(t);
            }
        }

        LogUtil.info("findByIdList idList is " + idList.toString() +
                ", total return " + retList.size() +
                " objects, class= " + getClass().getSimpleName());
        return retList;
    }

    public Map< String, T > findMapByIdList(Collection< String > idList) {
        if ( idList == null || idList.size() == 0 ) {
            LogUtil.warn(getReturnClass().getSimpleName() + " empty id list");
            return Collections.emptyMap();
        }

        Criteria criteria = Criteria.where("id").in(idList);
        if ( hasDeletedField() ) {
            criteria.and(FIELD_DELETED).ne(true);
        }

        List< T > list = mongoOperations.find(Query.query(criteria), getReturnClass());
        if ( list == null || list.size() == 0 ) {
            LogUtil.warn(getReturnClass().getSimpleName() + " idList is  " + idList.toString() + ", no data found");
            return Collections.emptyMap();
        }

        HashMap< String, T > map = new HashMap< String, T >();
        for ( T t : list ) {
            map.put(t.getId(), t);
        }

        // make the map objects sequence the same as input id list
        HashMap< String, T > retMap = new LinkedHashMap<>();
        for ( String id : idList ) {
            if ( map.containsKey(id) ) {
                retMap.put(id, map.get(id));
            }
        }

        LogUtil.info("findMapByIdList idList is " + idList.toString() +
                ", total return " + retMap.size() +
                " objects, class= " + getClass().getSimpleName());
        return retMap;
    }

    public boolean isExisted(String key, Object value) {
        Criteria criteria = Criteria.where(key).is(value);
        if ( hasDeletedField() ) {
            criteria.and(FIELD_DELETED).ne(true);
        }

        Query query = Query.query(criteria);
        return mongoOperations.exists(query, getReturnClass());
    }

}
