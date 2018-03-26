package com.vadim.vadblog.dao;

import com.vadim.vadblog.router.BlogRouter;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class Repository extends DataBaseBehavior{


    public Repository(Vertx vertx) {
        super(vertx);
    }

    @Override
    public void getAllPosts(RoutingContext routingContext) {
        JsonObject query = new JsonObject();
        getClient().find(COLLECTION, query, listAsyncResult -> {
            if (listAsyncResult.succeeded()) {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .putHeader(BlogRouter.ACCESS_CONTROL_ALLOW_ORIGIN, BlogRouter.HTTP_HEADER_SELECT_ALL)
                        .end(String.valueOf(listAsyncResult.result()));
            } else {
                listAsyncResult.cause().printStackTrace();
            }
        });
    }

    @Override
    public void save(JsonObject object) {
        getClient().save(COLLECTION, object, res -> { });
    }

    @Override
    public void edit(JsonObject object, JsonObject newObject) {
        getClient().findOneAndReplace(COLLECTION, object, newObject, res -> {
            if (res.succeeded()){
                System.out.println(res.result());
            }else{
                res.cause().printStackTrace();
            }
        });
    }

    @Override
    public void remove(JsonObject object) {
        getClient().removeDocument(COLLECTION, object, res -> {});
    }
}
