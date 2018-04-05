package com.vadim.vadblog;

import com.vadim.vadblog.dao.RepositoryVertical;
import com.vadim.vadblog.router.BlogRouter;
import com.vadim.vadblog.service.security.AuthManage;
import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(new RepositoryVertical());
        AuthManage security = new AuthManage(vertx);
        BlogRouter controller = new BlogRouter(vertx, security);
        controller.runRouter();

        vertx.createHttpServer()
                .requestHandler(controller.getRouter()::accept)
                .listen(8081);



    }

}
