package com.vadim.vadblog.service;

import com.vadim.vadblog.dao.model.ModelConstants;
import com.vadim.vadblog.dao.model.Post;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class JsonMaker {

    public JsonObject newJsonOfPost (Post post) {
        return new JsonObject()
                .put(ModelConstants.KEY_ID, post.getId())
                .put(ModelConstants.KEY_AUTHOR, post.getAuthor())
                .put(ModelConstants.KEY_TEXT, post.getText())
                .put(ModelConstants.KEY_DATE, post.getDate())
                .put(ModelConstants.KEY_VISIABLE, post.getVisiable());
    }

    public JsonObject titleJson (String title) {
        return new JsonObject()
                .put(ModelConstants.KEY_ID, title);
    }

    public List<JsonObject> updateDateOfPosts (List<JsonObject> posts) {
        for (JsonObject post : posts) {
            String date = post.getString("date");
            long executedDate = Long.parseLong(date);
            post.put("date", executedDate);
        }
        return posts;
    }

}
