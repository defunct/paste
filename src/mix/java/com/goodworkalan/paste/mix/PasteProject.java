package com.goodworkalan.paste.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Builds the project definition for Paste.
 *
 * @author Alan Gutierrez
 */
public class PasteProject implements ProjectModule {
    /**
     * Build the project definition for Paste.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.paste/paste/0.3.0.4")
                .depends()
                    .production("org.slf4j/slf4j-api/1.4.2")
                    .production("com.github.bigeasy.ilk/ilk-inject/0.+1")
                    .production("com.github.bigeasy.ilk/ilk-association/0.+1")
                    .production("com.github.bigeasy.infuse/infuse/0.+1")
                    .production("com.github.bigeasy.dovetail/dovetail/0.+7/")
                    .production("com.github.bigeasy.winnow/winnow/0.+1")
                    .production("javax.servlet/servlet-api/2.5")
                    .development("org.slf4j/slf4j-log4j12/1.4.2")
                    .development("org.eclipse.jetty/jetty-xml/7.1.4.v20100610")
                    .development("org.eclipse.jetty/jetty-webapp/7.1.4.v20100610")
                    .development("org.eclipse.jetty/jetty-plus/7.1.4.v20100610")
                    .development("org.eclipse.jetty/jetty-jndi/7.1.4.v20100610")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}
