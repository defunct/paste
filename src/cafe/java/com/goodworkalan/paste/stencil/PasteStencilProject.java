package com.goodworkalan.paste.stencil;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

/**
 * Builds the project definition for Paste Stencil.
 *
 * @author Alan Gutierrez
 */
public class PasteStencilProject implements ProjectModule {
    /**
     * Build the project definition for Paste Stencil.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.paste/paste-stencil/0.1")
                .depends()
                    .production("com.github.bigeasy.stencil/stencil/0.+1")
                    .production("com.github.bigeasy.paste/paste/0.+1")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
