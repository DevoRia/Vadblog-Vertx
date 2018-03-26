package com.vadim.vadblog.dao;

import com.vadim.vadblog.MainVerticle;
import com.vadim.vadblog.router.BlogRouter;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.ServerSocket;

import static org.junit.Assert.*;
public class RepositoryTest {

    DataBaseBehavior repository;
    Vertx vertx;

    @Before
    public void setUp() throws Exception {
        vertx = Vertx.vertx();
        repository = new Repository(vertx);
    }

    @After
    public void tearDown() throws Exception {
        repository.getClient().close();
    }

    @Test
    public void getAllPosts() {
        JsonObject query = new JsonObject();
        repository.getClient().find(DataBaseBehavior.COLLECTION, query, listAsyncResult -> {
            if (listAsyncResult.succeeded()) {
                System.out.println(listAsyncResult.result());
            } else {
                listAsyncResult.cause().printStackTrace();
            }
        });
    }
}