package com.vadim.vadblog.service;

import com.vadim.vadblog.dao.model.ModelConstants;
import com.vadim.vadblog.dao.model.Post;
import io.vertx.ext.web.RoutingContext;

import java.util.Date;

public class PostService implements Service{

    public Post makePost(RoutingContext routingContext, String username){
        return new Post(
                getAttribute(ModelConstants.KEY_TITLE, routingContext),
                username,
                getAttribute(ModelConstants.KEY_TEXT, routingContext),
                checkingDateExist(routingContext),
                false);
    }

    public static String getAttribute(String key, RoutingContext context) {
        return context.request().getFormAttribute(key);
    }

    private Long checkingDateExist(RoutingContext routingContext) {
        try {
            return Long.parseLong(getAttribute(ModelConstants.KEY_DATE, routingContext));
        } catch (NumberFormatException e) {
            return new Date().getTime();
        }
    }

}
