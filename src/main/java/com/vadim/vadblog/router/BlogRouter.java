package com.vadim.vadblog.router;

import com.vadim.vadblog.dao.model.Post;
import com.vadim.vadblog.service.PostService;
import com.vadim.vadblog.service.TransferDataService;
import com.vadim.vadblog.service.UserService;
import com.vadim.vadblog.service.security.AuthManage;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.KeycloakHelper;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.UserSessionHandler;

public class BlogRouter {

    public final static String ACCESS_CONTROL_ALLOW_ORIGIN =  "Access-Control-Allow-Origin";
    public final static String HTTP_HEADER_SELECT_ALL = "*";
    private final String SUCCESS_RESPONSE = "Success";

    private Router router;
    private AuthManage security;
    private JsonObject token;
    private AccessToken user;
    private PostService postService;
    private TransferDataService dataService;
    private UserService userService;

    public BlogRouter(Vertx vertx, AuthManage security) {
        this.dataService = new TransferDataService(vertx);
        this.security = security;
        this.postService = new PostService();
        this.userService = new UserService();
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setBodyLimit(-1));
        router.route().handler(UserSessionHandler.create(security.getKeycloakAuth()));
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
                .get("/data/username")
                .handler(this::getUsername);
        router
                .get("/data/isAdmin")
                .handler(this::isAdminChecked);
        router
                .get("/data/logout")
                .handler(this::loggingOut);
        router
                .get("/server/show")
                .handler(this::getAllPosts);
        router
                .post("/server/add")
                .handler(this::savePost);
        router
                .post("/server/edit")
                .handler(this::editPost);
        router
                .post("/server/remove/:id")
                .handler(this::remove);

    }

    private void loggingIn(RoutingContext routingContext) {
        token = KeycloakHelper.accessToken(routingContext.user().principal());
        user = (AccessToken) routingContext.user();
        routingContext.response()
                .setStatusCode(302)
                .putHeader("Location", "http://localhost:63342/VadBlog-Vertx/templates/index.html")
                .end();
    }

    private void getUsername (RoutingContext routingContext) {
        routingContext.response()
                .putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HTTP_HEADER_SELECT_ALL)
                .end(userService.getUsername(token));
    }

    private void isAdminChecked (RoutingContext routingContext){
        routingContext.response()
                .putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HTTP_HEADER_SELECT_ALL)
                .end(String.valueOf(userService.isAdmin(token)));
    }

    private void loggingOut (RoutingContext routingContext) {
        user.logout(res -> System.out.println("Logged out"));
        token = null;
        user = null;
        routingContext.response()
                .putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HTTP_HEADER_SELECT_ALL)
                .end();
    }

    private void getAllPosts (RoutingContext routingContext) {
        dataService.getAllPosts(res -> {
            if (res.succeeded()){
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .putHeader(BlogRouter.ACCESS_CONTROL_ALLOW_ORIGIN, BlogRouter.HTTP_HEADER_SELECT_ALL)
                        .end(String.valueOf(res.result().body()));
            }
        });
    }

    private void savePost (RoutingContext routingContext){
        if (token == null) return;
        Post post = postService.makePost(routingContext, userService.getUsername(token));
        dataService.save(post);
        routingContext.response()
                .putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HTTP_HEADER_SELECT_ALL)
                .end(SUCCESS_RESPONSE);
    }

    private void editPost (RoutingContext routingContext){
        if (userService.accessToChange(token, routingContext)) {
            Post post = postService.makePost(routingContext, userService.nameFromPost(routingContext));
            dataService.edit(post);
            routingContext.response()
                    .putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HTTP_HEADER_SELECT_ALL)
                    .end(SUCCESS_RESPONSE);
        }
    }

    private void remove (RoutingContext routingContext){
        if (userService.accessToChange(token, routingContext)){
            String id = routingContext.request().getParam("id");
            dataService.remove(id);
            routingContext.response()
                    .putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, HTTP_HEADER_SELECT_ALL)
                    .end(SUCCESS_RESPONSE);
        }
    }
}
