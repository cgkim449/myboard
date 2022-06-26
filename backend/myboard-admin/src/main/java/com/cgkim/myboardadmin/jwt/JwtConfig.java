package com.cgkim.myboardadmin.jwt;

import com.cgkim.myboardadmin.argumentresolver.IsAdminArgumentResolver;
import com.cgkim.myboardadmin.argumentresolver.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * jwtInterceptor, loginUserArgumentResolver 등록
 */
@RequiredArgsConstructor
@Configuration
public class JwtConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final IsAdminArgumentResolver isAdminArgumentResolver;

    /**
     * jwtInterceptor 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/members/login", "/members/signUp");
    }

    /**
     * loginUserArgumentResolver 등록
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(loginUserArgumentResolver);
        resolvers.add(isAdminArgumentResolver);
    }
}
