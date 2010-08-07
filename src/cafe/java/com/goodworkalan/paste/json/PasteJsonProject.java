package com.goodworkalan.stringbeans.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Builds the project definition for Paste JSON.
 *
 * @author Alan Gutierrez
 */
public class PasteJsonProject implements ProjectModule {
    /**
     * Build the project definition for Paste JSON.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.paste/paste-json/0.1.0.1")
                .depends()
                    .production("com.github.bigeasy.string-beans/string-beans-json/0.+1")
                    .production("com.github.bigeasy.paste/paste/0.+1")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.8.5")
                    .development("com.github.bigeasy.danger/danger-test/0.3.0.0")
                    .end()
                .end()
            .end();
    }
}
