package com.goodworkalan.paste.api;

import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PasteFilterTest {
    /** The Jetty server containing the <code>PasteFilter</code>. */
    private Server server;
    
    @BeforeTest
    public void start() throws Exception {
        server = new Server();
        
        SocketConnector connector = new SocketConnector();
        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(8086);
        server.setConnectors(new Connector[] { connector });
        
        WebAppContext context = new WebAppContext();
        
        server.setHandler(context);
        context.setServer(server);
        context.setContextPath("/");
        context.setWar("src/test/webapp");
        context.setConfigurationClasses(new String[] { 
                "org.eclipse.jetty.webapp.WebInfConfiguration",
                "org.eclipse.jetty.webapp.WebXmlConfiguration",
                "org.eclipse.jetty.plus.webapp.EnvConfiguration",
                "org.eclipse.jetty.plus.webapp.Configuration"
        });
        server.setHandler(context);

        context.getWebInf();
        
        server.start();
    }
    
    public List<String> slurp(InputStream in) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = buffer.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    @Test
    public void testConnection() throws Exception {
        URL url = new URL("http://localhost:8086/");
        List<String> lines = slurp(url.openStream());
        assertEquals(lines.size(), 1);
        assertEquals(lines.get(0), "Hello, World!");
    }
    
    
    @Test
    public void testForward() throws Exception {
        URL url = new URL("http://localhost:8086/forwarding");
        List<String> lines = slurp(url.openStream());
        assertEquals(lines.size(), 3);
        assertEquals(lines.get(0), "/forwarding");
        assertEquals(lines.get(1), "/forwarded");
        assertEquals(lines.get(2), "true");
    }
    
    @Test
    public void testControllerParameters() throws Exception {
        URL url = new URL("http://localhost:8086/controller/parameters/3?a=1&b=2");
        List<String> lines = slurp(url.openStream());
        assertEquals(lines.size(), 3);
        assertEquals(lines.get(0), "1");
        assertEquals(lines.get(1), "2");
        assertEquals(lines.get(2), "3");
    }
    
    
    @Test
    public void testInclude() throws Exception {
        URL url = new URL("http://localhost:8086/including?qs=true");
        List<String> lines = slurp(url.openStream());
        assertEquals(lines.size(), 7);
        assertEquals(lines.get(0), "");
        assertEquals(lines.get(1), "/included");
        assertEquals(lines.get(2), "/included");
        assertEquals(lines.get(3), "a=1&b=2");
        assertEquals(lines.get(4), "");
        assertEquals(lines.get(5), "1");
        assertEquals(lines.get(6), "2");
        url = new URL("http://localhost:8086/including");
        lines = slurp(url.openStream());
        assertEquals(lines.size(), 5);
        assertEquals(lines.get(0), "");
        assertEquals(lines.get(1), "/included");
        assertEquals(lines.get(2), "/included");
        assertEquals(lines.get(3), "null");
        assertEquals(lines.get(4), "");
    }
    
    @Test
    public void testJanitorSession() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8086/janitor/filter").openConnection();
        slurp(connection.getInputStream());
        String cookie = connection.getHeaderField("Set-Cookie").split(";")[0];
        connection = (HttpURLConnection) new URL("http://localhost:8086/janitor/session").openConnection();
        connection.setRequestProperty("Cookie", cookie);
        List<String> lines = slurp(connection.getInputStream());
        assertEquals(lines.size(), 1);
        assertEquals(lines.get(0), "filter");
        connection = (HttpURLConnection) new URL("http://localhost:8086/janitor/request").openConnection();
        connection.setRequestProperty("Cookie", cookie);
        slurp(connection.getInputStream());
        connection = (HttpURLConnection) new URL("http://localhost:8086/janitor/session").openConnection();
        connection.setRequestProperty("Cookie", cookie);
        lines = slurp(connection.getInputStream());
        assertEquals(lines.size(), 1);
        assertEquals(lines.get(0), "request");
    }
    
    @AfterTest
    public void stop() throws Exception {
        server.stop();
        server.join();
    }
}
