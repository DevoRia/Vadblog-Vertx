package com.vadim.vadblog.router;

import com.vadim.vadblog.dao.model.ModelConstants;
import com.vadim.vadblog.dao.model.Post;
import com.vadim.vadblog.service.TransferDataService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.Date;

public class BlogRouter {

    private Router router;
    private TransferDataService service;

    public BlogRouter(Vertx vertx) {
        service = new TransferDataService(vertx);
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setBodyLimit(-1));
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
        router
                .get("/data/logout")
                .handler(this::logout);
        router
                .get("/data/username")
                .handler(this::username);
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

    void username(RoutingContext routingContext) {
        AccessToken token = (AccessToken) routingContext.user();
        System.out.println(token);
//        System.out.println(token.idToken());
        routingContext
                .response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(/*token.accessToken().getString("username")*/  );
    }


    void logout(RoutingContext routingContext) {

        AccessToken token = (AccessToken) routingContext.user();
        token.userInfo(res -> {
            System.out.println(res.result());
        });

        User some = token.isAuthorized("some", booleanAsyncResult -> {
            token.logout(voidAsyncResult -> {
                System.out.println(voidAsyncResult.result());
            });
        });
        routingContext
                .response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end("Logged out");
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
