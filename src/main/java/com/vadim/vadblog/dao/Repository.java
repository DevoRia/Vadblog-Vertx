package com.vadim.vadblog.dao;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.*;

public class Repository extends DataBaseBehavior{

    private final String COLLECTION = "newBlog";
    volatile private ArrayList<JsonObject> foundAllPost = null;

    public Repository(Vertx vertx) {
        super(vertx);
    }

    @Override
    public List<JsonObject> getAllPosts() {
        JsonObject query = new JsonObject();
        getClient().find(COLLECTION, query, listAsyncResult -> {
            if (listAsyncResult.succeeded()) {
                foundAllPost = (ArrayList<JsonObject>) listAsyncResult.result();
            } else {
                listAsyncResult.cause().printStackTrace();
            }
        });
        return foundAllPost;
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
