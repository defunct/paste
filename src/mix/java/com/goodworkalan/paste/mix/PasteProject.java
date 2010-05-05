package com.goodworkalan.paste.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class PasteProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.paste/paste/0.2.1")
                .main()
                    .depends()
                        .include("org.slf4j/slf4j-api/1.4.2")
                        .include("com.github.bigeasy.string-beans/string-beans-url/0.+1")
                        .include("com.github.bigeasy.infuse/infuse/0.+1")
                        .include("com.github.bigeasy.dovetail/dovetail/0.+7/")
                        .include("aopalliance/aopalliance/1.0")
                        .include("com.google.inject/guice/2.0")
                        .include("com.google.inject/guice-servlet/2.0")
                        .include("com.github.bigeasy.deviate/deviate/0.+1")
                        .include("org.hibernate/hibernate-core/3.3.1.GA")
                        .include("org.hibernate/hibernate-annotations/3.4.0.GA")
                        .include("com.mallardsoft/tuple/2.0")
                        .include("javax.servlet/servlet-api/2.5")
                        .end()
                    .end()
                .test()
                    .depends()
                        .include("org.slf4j/slf4j-log4j12/1.4.2")
                        .include("org.eclipse.jetty/jetty-xml/7.0.0.RC3")
                        .include("org.eclipse.jetty/jetty-webapp/7.0.0.RC3")
                        .include("org.eclipse.jetty/jetty-plus/7.0.0.RC3")
                        .include("org.eclipse.jetty/jetty-jndi/7.0.0.RC3")
                        .include("org.testng/testng-jdk15/5.10")
                        .include("org.mockito/mockito-core/1.6")
                        .end()
                    .end()
                .end()
            .end();
    }
}
