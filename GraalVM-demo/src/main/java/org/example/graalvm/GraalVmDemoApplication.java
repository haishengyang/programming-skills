package org.example.graalvm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class GraalVmDemoApplication {

    private static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        System.out.println("=".repeat(60));
        System.out.println("🚀 GraalVM Demo Application Starting...");
        System.out.println("Start Time: " + startTime);
        System.out.println("=".repeat(60));

        SpringApplication.run(GraalVmDemoApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        long endTime = System.currentTimeMillis();
        long startupTime = endTime - startTime;

        // 获取实际分配的端口
        ServletWebServerApplicationContext context = (ServletWebServerApplicationContext) event.getApplicationContext();
        int port = context.getWebServer().getPort();

        System.out.println("=".repeat(60));
        System.out.println("✅ Application Ready!");
        System.out.println("🕐 Total Startup Time: " + startupTime + " ms");
        System.out.println("🌐 Access the application at: http://localhost:" + port);
        System.out.println("📊 Performance API: http://localhost:" + port + "/api/performance/info");
        System.out.println("⚡ Quick Test: http://localhost:" + port + "/api/performance/hello");
        System.out.println("🔧 All APIs: http://localhost:" + port + "/api/performance/");
        System.out.println("=".repeat(60));
    }
}
