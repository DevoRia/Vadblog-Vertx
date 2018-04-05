package com.vadim.vadblog.service;

import com.vadim.vadblog.VerticleConstatnts;
import com.vadim.vadblog.dao.model.Post;
import com.vadim.vadblog.router.BlogRouter;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;


public class TransferDataService implements Service{

    private EventBus eventBus;

    public TransferDataService(Vertx vertx) {
        this.eventBus = vertx.eventBus();
    }

    public void getAllPosts (RoutingContext routingContext/*Handler<AsyncResult<Message<String>>> handler*/) {
        eventBus.send(VerticleConstatnts.GET_POSTS, null, res -> {
            if (res.succeeded()){
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .putHeader(BlogRouter.ACCESS_CONTROL_ALLOW_ORIGIN, BlogRouter.HTTP_HEADER_SELECT_ALL)
                        .end(String.valueOf(res.result().body()));
            }
        });
    }

    public void save (Post post) {
        JsonObject object = JsonMaker.newJsonOfPost(post);
        eventBus.send(VerticleConstatnts.NEW_POST, object);
    }

    public void remove (String title) {
        JsonObject object = JsonMaker.titleJson(title);
        eventBus.send(VerticleConstatnts.REMOVE_POST, object);
    }

    public void edit(Post newPost) {
        JsonObject object = JsonMaker.newJsonOfPost(newPost);
        JsonObject titleObject = JsonMaker.titleJson(newPost.getId());
        JsonObject all = new JsonObject().put("old", titleObject);
        all.put("new", object);
        eventBus.send(VerticleConstatnts.EDIT_POST, all);
    }
}
