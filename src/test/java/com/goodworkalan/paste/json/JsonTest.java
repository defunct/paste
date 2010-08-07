package com.goodworkalan.paste.json;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.servlet.BindKey;
import com.goodworkalan.paste.servlet.Cassette;
import com.goodworkalan.winnow.RuleMap;

/**
 * Shows that the Paste Json project is of reasonably sound implementation. 
 *
 * @author Alan Gutierrez
 */
public class JsonTest {
    /**
     * The Json class correctly implements the Paste renderer extension
     * constructor returning a new instance of the given class.
     */
    @Test
    public void construct() {
        Connector connector = new Connector(new Cassette());
        Json json = connector.render().with(Json.class);
        assertEquals(Json.class, json.getClass());
    }

    /**
     * Build a Json renderer extension and return the modules.
     * 
     * @return The list of injetor modules.
     */
    private List<List<InjectorBuilder>> getModules(String callback) {
        Cassette cassette = new Cassette();
        Connector connector = new Connector(cassette);
        connector.render().suffix("js").with(Json.class).callback(callback).end();
        RuleMap<BindKey, List<InjectorBuilder>> rules = cassette.renderers.newRuleMap();
        Map<BindKey, Object> conditions = new HashMap<BindKey, Object>();
        conditions.put(BindKey.SUFFIX, "js");
        return rules.get(conditions);
    }

    /**
     * The correctly constructed Json class will add a module definition to the
     * the rule map.
     */
    @Test
    public void addModule() {
        assertEquals(1, getModules(null).size());
    }
    
    public Renderer newInstance(String callback, final StringWriter writer, Class<?> controllerClass) {
        // I find this bit to be tricky. Need to consider binding an inheritence in Ilk.
        InjectorBuilder newInjector = new InjectorBuilder();
        Injector injector = newInjector.newInjector();
        final Ilk.Box controller;
        try {
            controller = injector.getVendor(new Ilk.Key(controllerClass), Controller.class).get(injector);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        newInjector = injector.newInjector();
        for (InjectorBuilder module : getModules(callback).get(0)) {
            newInjector.module(module);
        }
        newInjector.module(new InjectorBuilder() { 
            protected void build() {
                try {
                    HttpServletResponse response = mock(HttpServletResponse.class);
                    when(response.getWriter()).thenReturn(new PrintWriter(writer));
                    instance(response, ilk(HttpServletResponse.class), null);
                    box(controller, ilk(Object.class), Controller.class);
                } catch (IOException e) {
                    // Mocked and should not be thrown.
                }
            }
        });
        return newInjector.newInjector().instance(Renderer.class, null);
    }

    /**
     * The modules are constructed and their bindings applied when the injector
     * is constructed.
     */
    @Test
    public void bindImplementations() {
        assertEquals(JsonRenderer.class, newInstance(null, new StringWriter(), WithoutCallback.class).getClass());
    }
    
    @Test
    public void renderJSON() {
        try {
            StringWriter writer = new StringWriter();
            newInstance(null, writer, WithoutCallback.class).render();
            assertEquals("{\n  \"a\":1\n}", writer.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    public void renderJSONP() {
        try {
            StringWriter writer = new StringWriter();
            newInstance("callback", writer, WithCallback.class).render();
            assertEquals("invoke({\n  \"a\":1\n});", writer.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    public void renderWhenCallbackIsNull() {
        try {
            StringWriter writer = new StringWriter();
            newInstance("callback", writer, WithoutCallback.class).render();
            assertEquals("{\n  \"a\":1\n}", writer.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    public void renderOutputMethod() {
        try {
            StringWriter writer = new StringWriter();
            newInstance(null, writer, WithOutputMethod.class).render();
            assertEquals("{\n  \"a\":1\n}", writer.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
