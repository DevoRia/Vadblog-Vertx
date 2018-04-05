package com.vadim.vadblog.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class Repository extends DataBaseBehavior{


    public Repository(Vertx vertx) {
        super(vertx);
    }

    @Override
    public void getAllPosts(Handler<AsyncResult<List<JsonObject>>> handler){
        JsonObject query = new JsonObject();
        getClient().find(COLLECTION, query, handler);
    }

    @Override
    public void save(JsonObject object) {
        getClient().save(COLLECTION, object, res -> { });
    }

    @Override
    public void edit(JsonObject object, JsonObject newObject) {
        getClient().findOneAndReplace(COLLECTION, object, newObject, res -> {
            if (res.succeeded()){
                System.out.println(res.result());
            }else{
                res.cause().printStackTrace();
            }
        });
    }

    @Override
    public void remove(JsonObject object) {
        getClient().removeDocument(COLLECTION, object, res -> {});
    }
}
