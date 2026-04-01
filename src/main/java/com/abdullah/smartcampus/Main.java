package com.abdullah.smartcampus;

import com.abdullah.smartcampus.config.ApplicationConfig;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {

    public static final String BASE_URI = "http://localhost:9095/api/";

    public static HttpServer startServer() {
        final ResourceConfig config = ResourceConfig
                .forApplication(new ApplicationConfig())
                .packages("com.abdullah.smartcampus");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = startServer();
        System.out.println("Server started at " + BASE_URI);
        System.out.println("Open this URL: " + BASE_URI + "v1");
        Thread.sleep(Long.MAX_VALUE);
        server.shutdownNow();
    }
}