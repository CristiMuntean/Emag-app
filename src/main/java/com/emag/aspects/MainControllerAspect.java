package com.emag.aspects;

import org.apache.commons.io.FileUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Aspect
@Component
public class MainControllerAspect {
    @Around("execution(* com.emag.controller.MainController.welcome(..))")
    public Object afterWelcome(ProceedingJoinPoint joinPoint){
        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        try {
            String newText = new String(Files.readAllBytes(Path.of("src/main/resources/templates/newIndex.html")),StandardCharsets.UTF_8);
            Document newDocument = Jsoup.parse(newText);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();
                newDocument.select("nav").get(0)
                        .select("div.container-fluid").get(0)
                        .select("p").get(0).text("Hello, " + currentUserName);
            }
            else {
                newDocument.select("nav").get(0)
                        .select("div.container-fluid").get(0)
                        .select("p").get(0).text("Hello, someone");
            }
            File indexFile = new File("src/main/resources/templates/newestIndex.html");
            FileUtils.writeStringToFile(indexFile, newDocument.toString(), StandardCharsets.UTF_8);
            return "newestIndex";
        } catch (IOException e) {
            return result;
        }
    }

    @Around("execution(* com.emag.controller.MainController.searchProducts(..))")
    public Object searchByName(ProceedingJoinPoint joinPoint) {
        String name = joinPoint.getArgs()[0].toString();
        System.out.println(name);
        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        try {
            String newText = new String(Files.readAllBytes(Path.of("src/main/resources/templates/newIndex.html")),StandardCharsets.UTF_8);
            Document newDocument = Jsoup.parse(newText);
            newDocument.select("nav").get(0)
                    .select("div.container-fluid").get(0)
                    .select("p").get(0).text("Search results that contain: " + name);
            File indexFile = new File("src/main/resources/templates/newestIndex.html");
            FileUtils.writeStringToFile(indexFile, newDocument.toString(), StandardCharsets.UTF_8);
            return "newestIndex";
        } catch (IOException e) {
            return result;
        }
    }

    @Around("execution(* com.emag.controller.MainController.cart(..))")
    public Object cart(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        try {
            String newText = new String(Files.readAllBytes(Path.of("src/main/resources/templates/newCart.html")),StandardCharsets.UTF_8);
            Document newDocument = Jsoup.parse(newText);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();
                newDocument.select("nav").get(0)
                        .select("div.container-fluid").get(0)
                        .select("p").get(0).text(currentUserName + "'s cart");
            }
            File indexFile = new File("src/main/resources/templates/newestCart.html");
            FileUtils.writeStringToFile(indexFile, newDocument.toString(), StandardCharsets.UTF_8);
            return "newestCart";
        } catch (IOException e) {
            return result;
        }
    }
}
