package com.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfig - Spring MVC view controllers
 * 
 * Updated for Security: "/" now redirects to /dashboard so DashboardController
 * can populate model attributes (stats) and Security can enforce authentication.
 * Direct view mapping would bypass controller logic and show empty stats.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to dashboard (requires authentication via SecurityConfig)
        registry.addRedirectViewController("/", "/dashboard");
        // Also handle favicon
        registry.addViewController("/403").setViewName("error/403");
    }
}
