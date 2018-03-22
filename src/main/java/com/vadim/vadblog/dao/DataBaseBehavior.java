package com.vadim.vadblog.dao;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

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

    public abstract List getAllPosts () throws InterruptedException;
    public abstract void save(JsonObject object);
    public abstract void edit(JsonObject object, JsonObject newObject);
    public abstract void remove(JsonObject object);

    MongoClient getClient() {
        return client;
    }


}
