package com.cgkim.myboard.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //TODO: 변수명 바꾸기
    private final String corsUrl1;
    private final String corsUrl2;
    private final String uploadLocation;

    public WebMvcConfig(
            @Value("${cors.url1}") String corsUrl1,
            @Value("${cors.url2}") String corsUrl2,
            @Value("${spring.servlet.multipart.location}") String uploadLocation
    ) {
        this.corsUrl1 = corsUrl1;
        this.corsUrl2 = corsUrl2;
        this.uploadLocation = uploadLocation;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsUrl1, corsUrl2)
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
