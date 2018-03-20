package com.vadim.vadblog.dao;

import com.vadim.vadblog.dao.model.Post;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

abstract class DataBaseBehavior {

    private final String DATABASE_URI = "mongodb://localhost:27017";
    private final String DATABASE_NAME = "blog";

    private JsonObject config;
    private MongoClient client;

    DataBaseBehavior(Vertx vertx) {
        this.config = setConfigSetting();
        client = MongoClient.createShared(vertx, this.config);
    }

    private JsonObject setConfigSetting() {
        return new JsonObject()
                .put("connection_string", DATABASE_URI)
                .put("db_name", DATABASE_NAME);
    }

    public abstract void getAllPosts ();
    public abstract void save (Post post);
    public abstract void edit (Post post);
    public abstract void remove (Post post);

    MongoClient getClient() {
        return client;
    }

}
