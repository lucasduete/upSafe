package io.github.lucasduete.upSafe;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

import io.github.lucasduete.upSafe.infraSecurity.CORSFilter;


@ApplicationPath("rest")
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        packages("io.github.lucasduete.upSafe.controllers");
        register(CORSFilter.class);
    }
}
