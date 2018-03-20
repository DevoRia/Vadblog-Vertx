package com.vadim.vadblog.router;

import com.vadim.vadblog.dao.Repository;
import com.vadim.vadblog.service.TransferDataService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class BlogRouter {

    private Router router;
    private Vertx vertx;
    private TransferDataService service;

    public BlogRouter(Vertx vertx) {
        this.vertx = vertx;
        service = new TransferDataService(this.vertx);
        router = Router.router(vertx);
        runRouter();
        service.getAllPosts();
    }

    public Router getRouter() {
        return router;
    }

    private void runRouter() {
        Route getAllPosts = router
                .get("/server/show")
                .handler(this::getAllPosts);

        Route savePost = router
                .post("/server/save");

        Route editPost = router
                .post("/server/edit");

        Route removePost = router
                .get("/server/remove/:id");
    }

   void getAllPosts (RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end();
    }


}
