package org.example.application;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.ws.rs.core.Application;

@QuarkusMain
public class QuarkusApplication extends Application {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}
