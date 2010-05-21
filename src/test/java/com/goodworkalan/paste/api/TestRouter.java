package com.goodworkalan.paste.api;

import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.connector.Router;
import com.goodworkalan.paste.forward.Forward;

public class TestRouter implements Router {
    public void connect(Connector connector) {
        connector
            .connect()
                .path("/forwarding").to(Forwarding.class).end()
                .path("/forwarded").to(Forwarded.class).end()
                .path("/including").to(Including.class).end()
                .path("/included").to(Included.class).end()
                .path("/controller/parameters/(c [0-9]+)").to(ControllerParameters.class).end()
                .path("/janitor")
                    .path("/filter").to(JanitorFilter.class).end()
                    .path("/request").to(JanitorRequest.class).end()
                    .path("/session").to(JanitorSession.class).end()
                    .end()
                .end();
        connector
            .render()
                .controller(Forwarding.class)
                .with(Forward.class)
                .property("steve")
                .format("/forwarded")
                .end();
    }
}
