package com.vadim.vadblog;

import com.vadim.vadblog.router.BlogRouter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        JsonObject config = Vertx.currentContext().config();
        BlogRouter controller = new BlogRouter(vertx);
        vertx.createHttpServer()
                .requestHandler(controller.getRouter()::accept)
                .listen(8081);
    }
}
