package com.goodworkalan.stringbeans.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

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
