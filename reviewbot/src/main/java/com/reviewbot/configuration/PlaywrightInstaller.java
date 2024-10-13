package com.reviewbot.configuration;

import com.microsoft.playwright.Playwright;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaywrightInstaller {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            System.out.println("Installing Playwright browsers...");
            playwright.chromium();
            playwright.firefox();
            playwright.webkit();
            System.out.println("Playwright browsers installed successfully.");
        }
    }
}
