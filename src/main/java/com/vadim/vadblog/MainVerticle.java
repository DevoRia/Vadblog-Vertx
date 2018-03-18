package com.vadim.vadblog;

import com.vadim.vadblog.Dao.Repository;
import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    private Repository repository = new Repository("blog", "blog");

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
              req.response()
                .putHeader("content-type", "text/plain")
                .end(String.valueOf(repository.getList()));
            }).listen(8080);
        System.out.println("HTTP server started on port 8080");
        repository.closeSession();
    }
}
