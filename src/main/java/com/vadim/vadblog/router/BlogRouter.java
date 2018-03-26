package com.vadim.vadblog.router;

import com.vadim.vadblog.dao.model.ModelConstants;
import com.vadim.vadblog.dao.model.Post;
import com.vadim.vadblog.service.TransferDataService;
import com.vadim.vadblog.service.security.KeycloakSecurity;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
    private AccessToken user;
    private String referrerUrl;


    public BlogRouter(Vertx vertx, KeycloakSecurity security) {
        service = new TransferDataService(vertx);
        this.security = security;
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setBodyLimit(-1));
        router.route().handler(UserSessionHandler.create(security.getoAuth2Auth()));
        security.getAuth().setupCallback(router.get("/callback"));
    }

    public Router getRouter() {
        return router;
    }

    public void runRouter() {
        router
                .get("/login")
                .handler(security.getAuth())
                .handler(this::loggingIn);
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

    private void loggingIn(RoutingContext routingContext) {
        token = KeycloakHelper.accessToken(routingContext.user().principal());
        user = (AccessToken) routingContext.user();
        routingContext.response()
                .setStatusCode(302)
                .putHeader("Location", "http://localhost:63342/VadBlog-Vertx/templates/index.html")
                .end();
    }

    private void getAllPosts (RoutingContext routingContext) {
        service.getAllPosts(routingContext);
    }

    private void savePost (RoutingContext routingContext){
        if (token == null) return;
        Post post = getParams(routingContext);
        service.save(post);
        routingContext.response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end("Success");
    }

    private void editPost (RoutingContext routingContext){
        String nameFromPost = getAttribute(ModelConstants.KEY_AUTHOR, routingContext);
        String nameFromToken = token.getString("preferred_username");
        //TODO: REFACTOR IT
        JsonObject resource_access = token.getJsonObject("realm_access");
        JsonArray roles = resource_access.getJsonArray("roles");

        if (nameFromToken.equals(nameFromPost) || roles.contains("admin")) {

            Post post = getParams(routingContext);
            post.setAuthor(nameFromPost);
            service.edit(post);
            routingContext.response()
                    .putHeader("Access-Control-Allow-Origin", "*")
                    .end("Success");
        }
    }

    private void remove (RoutingContext routingContext){
        if (routingContext.request().getFormAttribute("author").equals(token.getString("preferred_username"))) {
            String id = routingContext.request().getParam("id");
            service.remove(id);
            routingContext.response()
                    .putHeader("Access-Control-Allow-Origin", "*")
                    .end("Success");
        }
    }

    private void getUsername (RoutingContext routingContext) {
        String username = token.getString("preferred_username");
        routingContext.response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(username);
    }

    private void isAdminChecked (RoutingContext routingContext){
        JsonObject resource_access = token.getJsonObject("realm_access");
        JsonArray roles = resource_access.getJsonArray("roles");
        routingContext.response()
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(roles.contains("admin")));
    }

    private void loggingOut (RoutingContext routingContext) {
        user.logout(res -> System.out.println(res.result()));
        token = null;
        user = null;
        routingContext.response()
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
                token.getString("preferred_username"),
                getAttribute(ModelConstants.KEY_TEXT, routingContext),
                date,
                false);
    }

    private String getAttribute(String key, RoutingContext context) {
        return context.request().getFormAttribute(key);
    }


}
