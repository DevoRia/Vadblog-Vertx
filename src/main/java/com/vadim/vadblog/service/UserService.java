package com.vadim.vadblog.service;

import com.vadim.vadblog.dao.model.ModelConstants;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class UserService implements Service {

    public String getUsername(JsonObject token){
        return token.getString(USERNAME);
    }

    public Boolean isAdmin (JsonArray roles){
        return roles.contains(ADMIN_ROLE);
    }

    public Boolean isAdmin (JsonObject token){
        return getRolesByToken(token).contains(ADMIN_ROLE);
    }

    Boolean isAuthor (RoutingContext routingContext, JsonObject token){
        String nameFromToken = getUsername(token);
        return nameFromPost(routingContext).equals(nameFromToken);
    }

    JsonArray getRolesByToken (JsonObject token){
        JsonObject resource_access = token.getJsonObject(REALM_ACCESS);
        return resource_access.getJsonArray(ROLES_ARRAY);
    }

    public Boolean accessToChange (JsonObject token, RoutingContext routingContext) {
        return (isAuthor(routingContext, token) || isAdmin(token));
    }

    public String nameFromPost (RoutingContext routingContext){
        return PostService.getAttribute(ModelConstants.KEY_AUTHOR, routingContext);
    }
}
