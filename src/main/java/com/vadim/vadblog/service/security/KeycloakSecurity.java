package com.vadim.vadblog.service.security;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.OAuth2AuthHandler;

public class KeycloakSecurity {

    private OAuth2AuthHandler auth;

    public KeycloakSecurity (Vertx vertx, Router router) {
        auth = OAuth2AuthHandler.create(KeycloakAuth.create(vertx, OAuth2FlowType.AUTH_CODE, new JsonObject(
                "{\n" +
                        "  \"realm\": \"Blog\",\n" +
                        "  \"auth-server-url\": \"http://localhost:8180/auth\",\n" +
                        "  \"ssl-required\": \"external\",\n" +
                        "  \"resource\": \"vertx\",\n" +
                        "  \"credentials\": {\n" +
                        "    \"secret\": \"aadc82e9-f3d4-4af3-9c3d-102f6344adad\"\n" +
                        "  },\n" +
                        "  \"confidential-port\": 0\n" +
                        "}")), "http://localhost:8081");
        auth.setupCallback(router.get("/callback"));
        router.route("/server/*").handler(auth);
    }

    public OAuth2AuthHandler getAuth() {
        return auth;
    }
}
