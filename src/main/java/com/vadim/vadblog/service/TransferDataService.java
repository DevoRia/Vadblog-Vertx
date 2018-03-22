package com.vadim.vadblog.service;

import com.vadim.vadblog.dao.Repository;
import com.vadim.vadblog.dao.model.Post;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Date;

public class TransferDataService {

    private Vertx vertx;
    private Repository repository;
    private JsonMaker jsonMaker;

    public TransferDataService(Vertx vertx) {
        this.vertx = vertx;
        this.repository = new Repository(this.vertx);
        jsonMaker = new JsonMaker();
    }

    public List<JsonObject> getAllPosts() {
        return repository.getAllPosts();
    }

    public void save (Post post) {
        JsonObject object = jsonMaker.newJsonOfPost(post);
        System.out.println(object);
        repository.save(object);
    }

    public void remove (String title) {
        JsonObject object = jsonMaker.titleJson(title);
        repository.remove(object);
    }

    public void edit(Post newPost) {
        JsonObject object = jsonMaker.newJsonOfPost(newPost);
        JsonObject titleObject = jsonMaker.titleJson(newPost.getId());
        System.out.println(object + " NEW ");
        System.out.println(titleObject + " TITLE ");
        repository.edit(titleObject, object);
    }
}
