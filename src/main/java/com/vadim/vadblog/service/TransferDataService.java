package com.vadim.vadblog.service;

import com.vadim.vadblog.dao.Repository;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class TransferDataService {

    private Vertx vertx;
    private Repository repository;

    public TransferDataService(Vertx vertx) {
        this.vertx = vertx;
        this.repository = new Repository(this.vertx);
    }

    public void getAllPosts() {
        repository.getAllPosts();
    }

    public void save () {

    }

    public void remove () {

    }


}
