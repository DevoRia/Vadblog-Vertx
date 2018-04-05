package com.vadim.vadblog.service;

import com.vadim.vadblog.VerticleConstatnts;
import com.vadim.vadblog.dao.model.Post;
import com.vadim.vadblog.router.BlogRouter;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class TransferDataService implements Service{

    private EventBus eventBus;

    public TransferDataService(Vertx vertx) {
        this.eventBus = vertx.eventBus();
    }

    public void getAllPosts (Handler<AsyncResult<Message<String>>> handler) {
        eventBus.send(VerticleConstatnts.GET_POSTS, null, handler);
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
        JsonObject all = new JsonObject()
                .put(VerticleConstatnts.EDIT_OLD_POST, titleObject)
                .put(VerticleConstatnts.EDIT_NEW_POST, object);
        eventBus.send(VerticleConstatnts.EDIT_POST, all);
    }
}
