package com.emag.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = {"com.emag.services", "com.emag.aspects", "com.emag.controller"})
@EnableAspectJAutoProxy
public class ProjectConfig {

}
