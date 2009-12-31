package com.goodworkalan.paste.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class PasteProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces(new Artifact("com.goodworkalan/paste/0.2.1"))
                .main()
                    .depends()
                        .artifact(new Artifact("org.slf4j/slf4j-api/1.4.2"))
                        .artifact(new Artifact("com.goodworkalan/infuse/0.1"))
                        .artifact(new Artifact("com.goodworkalan/dovetail/0.7"))
                        .artifact(new Artifact("aopalliance/aopalliance/1.0"))
                        .artifact(new Artifact("com.google.inject/guice/2.0"))
                        .artifact(new Artifact("com.google.inject/guice-servlet/2.0"))
                        .artifact(new Artifact("com.goodworkalan/deviate/0.1"))
                        .artifact(new Artifact("org.hibernate/hibernate-core/3.3.1.GA"))
                        .artifact(new Artifact("org.hibernate/hibernate-annotations/3.4.0.GA"))
                        .artifact(new Artifact("com.mallardsoft/tuple/2.0"))
                        .artifact(new Artifact("javax.servlet/servlet-api/2.5"))
                        .end()
                    .end()
                .test()
                    .depends()
                        .artifact(new Artifact("org.slf4j/slf4j-log4j12/1.4.2"))
                        .artifact(new Artifact("log4j/log4j-log4j12/1.4.2"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-xml/7.0.0.RC3"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-webapp/7.0.0.RC3"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-plus/7.0.0.RC3"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-jndi/7.0.0.RC3"))
                        .artifact(new Artifact("org.testng/testng/5.10/jdk15"))
                        .artifact(new Artifact("org.mockito/mockito-core/1.6"))
                        .end()
                    .end()
                .end()
            .end();
    }
}
