package io.fiber.net.plugin.example;

import io.fiber.net.common.ext.RouterNameFetcher;
import io.fiber.net.proxy.LibProxyMainModule;
import io.fiber.net.proxy.ScriptHandler;
import io.fiber.net.server.HttpEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ExampleMain {
    public static final Logger log = LoggerFactory.getLogger(ExampleMain.class);

    public static void main(String[] args) throws Exception {
        HttpEngine engine = LibProxyMainModule.createEngineWithSPI(null);
        addJsProject(engine, new File("scripts/dubbo.js"));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> engine.getInjector().destroy()));
    }

    private static void addJsProject(HttpEngine engine, File listFile) {
        try {
            byte[] bytes = Files.readAllBytes(listFile.toPath());
            ScriptHandler project = LibProxyMainModule.createProject(engine.getInjector(),
                    RouterNameFetcher.DEF_ROUTER_NAME,
                    new String(bytes, StandardCharsets.UTF_8));
            engine.addHandlerRouter(project);
        } catch (Exception e) {
            log.error("error init project", e);
        }
    }
}
