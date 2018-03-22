package com.vadim.vadblog.router;

import com.vadim.vadblog.dao.model.ModelConstants;
import com.vadim.vadblog.dao.model.Post;
import com.vadim.vadblog.service.TransferDataService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.Date;

public class BlogRouter {

    private Router router;
    private MainRouter mainRouter;
    private TransferDataService service;

    public BlogRouter(Vertx vertx) {
        service = new TransferDataService(vertx);
        mainRouter = MainRouter.getInstance(vertx);
        router = mainRouter.getRouter();
        runRouter();
    }

    public Router getRouter() {
        return router;
    }

    private void runRouter() {
        router
                .get("/server/show")
                .handler(this::getAllPosts);
        router
                .route(HttpMethod.POST, "/server/add")
                .handler(this::savePost);
        router
                .route(HttpMethod.POST, "/server/edit")
                .handler(this::editPost);
        router
                .get("/server/remove/:id")
                .handler(this::remove);
    }

    void getAllPosts (RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(service.getAllPosts()));

    }

    void savePost (RoutingContext routingContext){
        Post post = getParams(routingContext);
        service.save(post);
        routingContext.response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end("Success");
    }

    void editPost (RoutingContext routingContext){
        Post post = getParams(routingContext);
        service.edit(post);
        routingContext.response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end("Success");
    }

    void remove (RoutingContext routingContext){
        String id = routingContext.request().getParam("id");
        service.remove(id);
        routingContext.response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end("Success");

    }

    private Post getParams(RoutingContext routingContext){
        Long date;

        try {
            date = Long.parseLong(getAttribute(ModelConstants.KEY_DATE, routingContext));
        } catch (NumberFormatException e) {
            date = new Date().getTime();
        }

        return new Post(
                getAttribute(ModelConstants.KEY_TITLE, routingContext),
                getAttribute(ModelConstants.KEY_AUTHOR, routingContext),
                getAttribute(ModelConstants.KEY_TEXT, routingContext),
                date,
                false);
    }

    private String getAttribute(String key, RoutingContext context) {
        return context.request().getFormAttribute(key);
    }


}
