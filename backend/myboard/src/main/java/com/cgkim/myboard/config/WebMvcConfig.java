package com.cgkim.myboard.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * cors 설정, ResourceHandlers 등록
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final String corsUrl;

    private final String uploadLocation;

    /**
     * cors 설정 값, 첨부파일 업로드 경로 값 주입
     *
     * @param corsUrl
     * @param uploadLocation
     */
    public WebMvcConfig(@Value("${cors.url}") String corsUrl,
                        @Value("${spring.servlet.multipart.location}") String uploadLocation
    ) {

        this.corsUrl = corsUrl;
        this.uploadLocation = uploadLocation;
    }

    /**
     * cors 설정
     *
     * @param registry
     */
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
                        HttpMethod.DELETE.name()
                )
                .exposedHeaders("Content-Disposition", "Authorization", "Location");
    }

    /**
     * ResourceHandler 등록
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + uploadLocation + "/");
    }
}
