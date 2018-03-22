package com.vadim.vadblog.router;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainRouter {

    private Router router;
    private BodyHandler bodyHandler;
    private Vertx vertx;

    private static MainRouter ourInstance;

    public static MainRouter getInstance() {
        return ourInstance;
    }

    public Router getRouter() {
        return router;
    }

    public static MainRouter getInstance(Vertx vertx){
        return ourInstance = new MainRouter(vertx);
    }

    private MainRouter(Vertx vertx) {
        bodyHandler = BodyHandler.create().setBodyLimit(-1);
        router.route().handler(bodyHandler);
    }



}
