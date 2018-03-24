package com.vadim.vadblog.router;

import com.vadim.vadblog.dao.model.ModelConstants;
import com.vadim.vadblog.dao.model.Post;
import com.vadim.vadblog.service.TransferDataService;
import com.vadim.vadblog.service.security.KeycloakSecurity;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.KeycloakHelper;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.UserSessionHandler;

import java.util.Date;

public class BlogRouter {

    private Router router;
    private TransferDataService service;
    private KeycloakSecurity security;
    private JsonObject token;

    public BlogRouter(Vertx vertx, KeycloakSecurity security) {
        service = new TransferDataService(vertx);
        this.security = security;
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setBodyLimit(-1));
        router.route().handler(UserSessionHandler.create(security.getoAuth2Auth()));
        security.getAuth().setupCallback(router.get("/callback"));
        router.route().handler(security.getAuth());
    }

    public Router getRouter() {
        return router;
    }

    public void runRouter() {
        router.route("/login")
                .handler(security.getAuth())
               ;
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
                .get("/data/username")
                .handler(this::getUsername);
        router
                .get("/data/isAdmin")
                .handler(this::isAdminChecked);
        router
                .get("/data/logout")
                .handler(this::loggingOut);
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

    void getUsername (RoutingContext routingContext) {
        token = KeycloakHelper.accessToken(routingContext.user().principal());
        String username = token.getString("preferred_username");
        routingContext.response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(username);
    }

    void isAdminChecked (RoutingContext routingContext){
        token = KeycloakHelper.accessToken(routingContext.user().principal());
        JsonObject resource_access = token.getJsonObject("realm_access");
        JsonArray roles = resource_access.getJsonArray("roles");
        routingContext.response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(roles.contains("admin")));
    }

    void loggingOut (RoutingContext routingContext) {
        AccessToken user = (AccessToken) routingContext.user();
        user.logout(res -> System.out.println(res.result()));
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
