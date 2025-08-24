package org.example.graalvm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;

@Controller
public class DemoController {

    @GetMapping("/")
    public String index(Model model) {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        
        // 添加基本信息到模型
        model.addAttribute("jvmName", runtimeBean.getVmName());
        model.addAttribute("jvmVersion", runtimeBean.getVmVersion());
        model.addAttribute("startupTime", runtimeBean.getUptime());
        model.addAttribute("heapUsed", memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024));
        model.addAttribute("heapMax", memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024));
        
        return "index";
    }
}
