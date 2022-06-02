package com.cgkim.myboard.config.jwt;

import com.cgkim.myboard.argumentresolver.GuestArgumentResolver;
import com.cgkim.myboard.argumentresolver.CheckGuestPasswordArgumentResolver;
import com.cgkim.myboard.argumentresolver.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class JwtConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final GuestArgumentResolver guestArgumentResolver;
    private final CheckGuestPasswordArgumentResolver checkGuestPasswordArgumentResolver;

    /**
     * Interceptor 등록
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/users/login");
    }

    /**
     * ArgumentResolver 등록
     *
     * @param resolvers initially an empty list
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
        resolvers.add(guestArgumentResolver);
        resolvers.add(checkGuestPasswordArgumentResolver);
    }
}
