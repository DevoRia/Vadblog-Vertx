package com.vadim.vadblog;

import com.vadim.vadblog.router.BlogRouter;
import com.vadim.vadblog.service.security.KeycloakSecurity;
import io.vertx.core.AbstractVerticle;


public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        KeycloakSecurity security = new KeycloakSecurity(vertx, config());
        BlogRouter controller = new BlogRouter(vertx, security);

        controller.runRouter();

        vertx.createHttpServer()
                .requestHandler(controller.getRouter()::accept)
                .listen(8081);
    }

}
