package com.goodworkalan.paste.api;

import com.goodworkalan.paste.Connector;
import com.goodworkalan.paste.Router;
import com.goodworkalan.paste.forward.Forward;

public class TestRouter implements Router {
    public void connect(Connector connector) {
        connector
            .connect()
                .path("/forwarding").to(Forwarding.class).end()
                .path("/forwarded").to(Forwarded.class).end()
                .end();
        connector
            .view()
                .controller(Forwarding.class)
                    .with(Forward.class)
                    .format("/forwarded")
                    .end()
                .end();
    }
}
