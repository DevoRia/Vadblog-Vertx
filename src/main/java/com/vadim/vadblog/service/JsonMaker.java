package com.vadim.vadblog.service;

import com.vadim.vadblog.dao.model.ModelConstants;
import com.vadim.vadblog.dao.model.Post;
import io.vertx.core.json.JsonObject;

public class JsonMaker {

    public static JsonObject newJsonOfPost (Post post) {
        return new JsonObject()
                .put(ModelConstants.KEY_ID, post.getId())
                .put(ModelConstants.KEY_AUTHOR, post.getAuthor())
                .put(ModelConstants.KEY_TEXT, post.getText())
                .put(ModelConstants.KEY_DATE, post.getDate())
                .put(ModelConstants.KEY_VISIABLE, post.getVisiable());
    }

    public static JsonObject titleJson (String title) {
        return new JsonObject()
                .put(ModelConstants.KEY_ID, title);
    }
}
