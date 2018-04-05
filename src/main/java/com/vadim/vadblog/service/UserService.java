package com.vadim.vadblog.service;

import com.vadim.vadblog.dao.model.ModelConstants;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class UserService implements Service {

    private final String NULL_EXCEPTION_MESSAGE = "User isn't found";

    public String getUsername(JsonObject token){
        try {
            return token.getString(USERNAME);
        }catch (NullPointerException e){
            System.out.println(NULL_EXCEPTION_MESSAGE);
            return null;
        }
    }

    public Boolean isAdmin (JsonArray roles){
        return roles.contains(ADMIN_ROLE);
    }

    public Boolean isAdmin (JsonObject token){
        return getRolesByToken(token).contains(ADMIN_ROLE);
    }

    Boolean isAuthor (RoutingContext routingContext, JsonObject token){
        String nameFromToken = getUsername(token);
        return nameFromToken.equals(nameFromPost(routingContext));
    }

    JsonArray getRolesByToken (JsonObject token){
        try {
            JsonObject resource_access = token.getJsonObject(REALM_ACCESS);
            return resource_access.getJsonArray(ROLES_ARRAY);
        }catch (NullPointerException e){
            System.out.println(NULL_EXCEPTION_MESSAGE);
            return null;
        }

    }

    public Boolean accessToChange (JsonObject token, RoutingContext routingContext) {
        return (isAdmin(token) || isAuthor(routingContext, token));
    }

    public String nameFromPost (RoutingContext routingContext){
        return PostService.getAttribute(ModelConstants.KEY_AUTHOR, routingContext);
    }

}
