package com.datecourse.global.config;

import com.datecourse.web.argumentresolver.LoginArgumentResolver;
import com.datecourse.web.interceptor.LoginInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] WHITE_LIST = {"/", "/css/**", "/*.ico", "/error", "/js/**"};

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/datecourse/login", "/datecourse/signup")
                .excludePathPatterns(WHITE_LIST);
    }
}
