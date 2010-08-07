package com.goodworkalan.paste.json;

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;
import org.testng.annotations.Test;

import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.servlet.BindKey;
import com.goodworkalan.paste.servlet.Cassette;
import com.goodworkalan.stringbeans.json.paste.Json;
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
    private List<List<InjectorBuilder>> getModules() {
        Cassette cassette = new Cassette();
        Connector connector = new Connector(cassette);
        connector.render().suffix("js").with(Json.class).end();
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
        assertEquals(1, getModules().size());
    }

    /**
     * The modules are constructed and their bindings applied when the injector
     * is constructed.
     */
    @Test
    public void bindImplementations() {
        InjectorBuilder newInjector = new InjectorBuilder();
        for (InjectorBuilder module : getModules().get(0)) {
            newInjector.module(module);
        }
        final StringWriter writer = new StringWriter();
        newInjector.module(new InjectorBuilder() { 
            protected void build() {
                try {
                    HttpServletResponse response = mock(HttpServletResponse.class);
                    when(response.getWriter()).thenReturn(new PrintWriter(writer));
                    instance(response, ilk(HttpServletResponse.class), null);
                    instance(new Hello(), ilk(Object.class), Controller.class);
                } catch (IOException e) {
                    // Mocked and should not be thrown.
                }
            }
        });
      /*  Injector injector = */ newInjector.newInjector();
//        assertEquals(JsonRenderer.class, injector.instance(Renderer.class, null));
    }
}
