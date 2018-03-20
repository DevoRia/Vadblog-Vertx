package com.vadim.vadblog.dao;

import com.vadim.vadblog.dao.model.Post;
import com.vadim.vadblog.service.JsonMaker;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class Repository extends DataBaseBehavior{

    private JsonMaker jsonMaker;

    public Repository(Vertx vertx) {
        super(vertx);
        jsonMaker = new JsonMaker();
    }

    @Override
    public void getAllPosts() {
        JsonObject query = new JsonObject();
        MongoClient blog = getClient().find("blog", query, listAsyncResult -> {
            if (listAsyncResult.succeeded()) {
                for (JsonObject json : listAsyncResult.result()) {
                    System.out.println(json.encodePrettily());
                }
            } else {
                listAsyncResult.cause().printStackTrace();
            }
        });
    }

    @Override
    public void save(Post post) {

    }

    @Override
    public void edit(Post post) {

    }

    @Override
    public void remove(Post post) {

    }

}
