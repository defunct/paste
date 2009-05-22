package com.goodworkalan.paste.redirect;

import static com.goodworkalan.paste.paths.FormatArguments.REQUEST_DIRECTORY_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

import com.goodworkalan.paste.paths.FormatArgument;
import com.goodworkalan.paste.redirect.Configuration;

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
