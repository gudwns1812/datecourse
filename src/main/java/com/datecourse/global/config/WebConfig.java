package com.datecourse.global.config;

import com.datecourse.web.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] WHITE_LIST = {"/", "/css/**", "/*.ico", "/error", "/js/**"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/datecourse/login", "/datecourse/signup")
                .excludePathPatterns(WHITE_LIST);
    }
}
