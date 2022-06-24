package com.cgkim.myboard.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfig
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final String corsUrl;
    private final String uploadLocation;

    public WebMvcConfig(
            @Value("${cors.url}") String corsUrl,
            @Value("${spring.servlet.multipart.location}") String uploadLocation
    ) {
        this.corsUrl = corsUrl;
        this.uploadLocation = uploadLocation;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsUrl)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name()
                )
                .exposedHeaders("Content-Disposition", "Authorization", "Location");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///"+ uploadLocation +"/");
    }


}
