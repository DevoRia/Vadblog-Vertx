package com.vadim.vadblog.service.security;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth;
import io.vertx.ext.web.handler.OAuth2AuthHandler;

public class KeycloakSecurity {

    private OAuth2AuthHandler auth;
    private OAuth2Auth oAuth2Auth;

    public KeycloakSecurity (Vertx vertx, JsonObject config) {
        JsonObject object = new JsonObject(
                "{\n" +
                        "  \"realm\": \"blog\",\n" +
                        "  \"auth-server-url\": \"http://localhost:8180/auth\",\n" +
                        "  \"ssl-required\": \"external\",\n" +
                        "  \"resource\": \"vertx\",\n" +
                        "  \"credentials\": {\n" +
                        "    \"secret\": \"37499fd8-4755-4658-8362-64cf8fda0de2\"\n" +
                        "  },\n" +
                        "  \"confidential-port\": 0,\n" +
                        "  \"policy-enforcer\": {}\n" +
                        "}");
        oAuth2Auth = KeycloakAuth.create(vertx, OAuth2FlowType.AUTH_CODE, object);
        auth = OAuth2AuthHandler.create(oAuth2Auth, "http://localhost:8081");


    }

    public OAuth2AuthHandler getAuth() {
        return auth;
    }

    public OAuth2Auth getoAuth2Auth() {
        return oAuth2Auth;
    }
}
