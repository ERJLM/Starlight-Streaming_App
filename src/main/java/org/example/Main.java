package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URL;
import java.util.Optional;

public class Main {
    public static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
    public static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));
    public static void main(String[] args) throws LifecycleException {

        String contextPath = "/myapp" ;
        String appBase = ".";
        Tomcat tomcat = new Tomcat();
        //define port, host, contextpath
        tomcat.setPort(Integer.valueOf(PORT.orElse("9090") ));
        tomcat.setHostname(HOSTNAME.orElse("localhost"));
        tomcat.getHost().setAppBase(appBase);
        tomcat.addWebapp(contextPath, appBase);


        //annotation scanning
        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());
        ctx.addLifecycleListener(new ContextConfig());
        // Add the JAR/folder containing this class to PreResources
        final WebResourceRoot root = new StandardRoot(ctx);
        final URL url = findClassLocation(Main.class);
        root.createWebResourceSet(WebResourceRoot.ResourceSetType.PRE, "/WEB-INF/classes", url, "/");
        ctx.setResources(root);

        //create connection
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

    // Tries to find the URL of the JAR or directory containing {@code clazz}.
    private static URL findClassLocation(Class< ? > clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }
}
