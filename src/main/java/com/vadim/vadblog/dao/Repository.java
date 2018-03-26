package com.vadim.vadblog.dao;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.*;

public class Repository extends DataBaseBehavior{

    private final String COLLECTION = "newBlog";
    private ArrayList<JsonObject> foundAllPost = null;

    public Repository(Vertx vertx) {
        super(vertx);
    }



    @Override
    public void getAllPosts(RoutingContext routingContext) {
        JsonObject query = new JsonObject();
        getClient().find(COLLECTION, query, listAsyncResult -> {
            if (listAsyncResult.succeeded()) {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .putHeader("Access-Control-Allow-Origin", "*")
                        .end(String.valueOf(listAsyncResult.result()));
            } else {
                listAsyncResult.cause().printStackTrace();
            }
        });
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

    public ArrayList<JsonObject> getFoundAllPost() {
        return foundAllPost;
    }
}
