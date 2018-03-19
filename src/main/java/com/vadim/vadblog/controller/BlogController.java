package com.vadim.vadblog.controller;

import com.vadim.vadblog.dao.Repository;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class BlogController {

    private Router router;

    public BlogController (Vertx vertx) {
        router = Router.router(vertx);
        runRouter();
    }

    public Router getRouter() {
        return router;
    }

    private void runRouter() {
        Route getAllPosts = router
                .get("/server/show")
                .handler(this::getAllPosts);
    }

   void getAllPosts (RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encode(new Repository("blog", "blog").getList()));
    }


}
