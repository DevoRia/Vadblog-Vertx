package com.vadim.vadblog;

import com.vadim.vadblog.router.BlogRouter;
import com.vadim.vadblog.service.security.KeycloakSecurity;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.KeycloakHelper;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.OAuth2AuthHandler;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        BlogRouter controller = new BlogRouter(vertx);
        KeycloakSecurity security = new KeycloakSecurity(vertx, controller.getRouter());


        vertx.createHttpServer()
                .requestHandler(controller.getRouter()::accept)
                .listen(8081);
    }

}
