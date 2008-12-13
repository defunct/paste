package com.goodworkalan.guicelet.redirect;

import static com.goodworkalan.guicelet.paths.FormatArguments.REQUEST_DIRECTORY_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.paths.FormatArgument;

public class ConfigurationTest
{
    @Test
    public void constructor()
    {
        Configuration configuration = new Configuration(301, "%s/index", new FormatArgument[] { REQUEST_DIRECTORY_NAME });
        assertEquals(configuration.getStatus(), 301);
        assertEquals(configuration.getFormat(), "%s/index");
        assertSame(configuration.getFormatArguments()[0], REQUEST_DIRECTORY_NAME);
    }
}
