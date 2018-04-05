package com.vadim.vadblog.dao;

import com.vadim.vadblog.VerticleConstatnts;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class RepositoryVertical extends AbstractVerticle{

    @Override
    public void start() throws Exception {
        Repository repository = new Repository(vertx);
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(VerticleConstatnts.GET_POSTS, req -> {
            repository.getAllPosts(asyncResult -> {
                if (asyncResult.succeeded()){
                    req.reply(String.valueOf(asyncResult.result()));
                }
            });
        });

        eventBus.consumer(VerticleConstatnts.NEW_POST, req -> {
            repository.save((JsonObject) req.body());
        });

        eventBus.consumer(VerticleConstatnts.EDIT_POST, req -> {
            JsonObject body = (JsonObject) req.body();
            repository.edit(
                    body.getJsonObject(VerticleConstatnts.EDIT_OLD_POST),
                    body.getJsonObject(VerticleConstatnts.EDIT_NEW_POST));
        });

        eventBus.consumer(VerticleConstatnts.REMOVE_POST, req -> {
            repository.remove((JsonObject) req.body());
        });
    }
}
