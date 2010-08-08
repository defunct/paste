package com.goodworkalan.paste.infuse;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

/**
 * Builds the project definition for Paste Infuse.
 *
 * @author Alan Gutierrez
 */
public class PasteInfuseProject implements ProjectModule {
    /**
     * Build the project definition for Paste Infuse.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.paste/paste-infuse/0.1")
                .depends()
                    .production("com.github.bigeasy.string-beans/string-beans-url/0.+1")
                    .production("com.github.bigeasy.paste/paste/0.+1")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}
