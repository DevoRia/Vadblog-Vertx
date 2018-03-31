package com.vadim.vadblog.service.security;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth;
import io.vertx.ext.web.handler.OAuth2AuthHandler;

public class AuthManage {

    private OAuth2AuthHandler auth;
    private OAuth2Auth keycloakAuth;

    public AuthManage(Vertx vertx) {
        JsonObject object = new JsonObject(
                "{\n" +
                        "  \"realm\": \"Blog\",\n" +
                        "  \"auth-server-url\": \"http://localhost:8180/auth\",\n" +
                        "  \"ssl-required\": \"external\",\n" +
                        "  \"resource\": \"vertx\",\n" +
                        "  \"credentials\": {\n" +
                        "    \"secret\": \"7f3d78a1-62e1-4706-9021-20ff886436e7\"\n" +
                        "  },\n" +
                        "  \"confidential-port\": 0,\n" +
                        "  \"policy-enforcer\": {}\n" +
                        "}");
        keycloakAuth = KeycloakAuth.create(vertx, OAuth2FlowType.AUTH_CODE, object);
        auth = OAuth2AuthHandler.create(keycloakAuth, "http://localhost:8081");
    }

    public OAuth2AuthHandler getAuth() {
        return auth;
    }

    public OAuth2Auth getKeycloakAuth() {
        return keycloakAuth;
    }

}
