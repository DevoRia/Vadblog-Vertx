package com.vadim.vadblog.service;

import com.vadim.vadblog.dao.Repository;
import com.vadim.vadblog.dao.model.Post;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Date;

public class TransferDataService implements Service{

    private Repository repository;

    public TransferDataService(Vertx vertx) {
        this.repository = new Repository(vertx);
    }

    public void getAllPosts (RoutingContext routingContext) {
        repository.getAllPosts(routingContext);
    }

    public void save (Post post) {
        JsonObject object = JsonMaker.newJsonOfPost(post);
        repository.save(object);
    }

    public void remove (String title) {
        JsonObject object = JsonMaker.titleJson(title);
        repository.remove(object);
    }

    public void edit(Post newPost) {
        JsonObject object = JsonMaker.newJsonOfPost(newPost);
        JsonObject titleObject = JsonMaker.titleJson(newPost.getId());
        repository.edit(titleObject, object);
    }
}
