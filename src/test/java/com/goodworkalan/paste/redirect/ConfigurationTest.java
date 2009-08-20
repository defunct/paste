package com.goodworkalan.paste.redirect;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.paste.paths.PathDirectory;

public class ConfigurationTest
{
    @Test
    public void constructor()
    {
        Configuration configuration = new Configuration(301, "%s/index", new Class<?>[] { PathDirectory.class });
        assertEquals(configuration.getStatus(), 301);
        assertEquals(configuration.getFormat(), "%s/index");
        assertEquals(configuration.getFormatArguments()[0],  PathDirectory.class);
    }
}
