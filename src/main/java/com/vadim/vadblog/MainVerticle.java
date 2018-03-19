package com.vadim.vadblog;

import com.vadim.vadblog.controller.BlogController;
import com.vadim.vadblog.dao.Repository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import sun.security.provider.certpath.Vertex;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        BlogController controller = new BlogController(vertx);
        vertx.createHttpServer()
                .requestHandler(controller.getRouter()::accept)
                .listen(8081);
    }
}
