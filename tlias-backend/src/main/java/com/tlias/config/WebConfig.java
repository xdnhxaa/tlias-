package com.tlias.config;

import com.tlias.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web 配置：注册登录校验拦截器、放开上传目录、开发期跨域
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Value("${tlias.upload.dir:./uploads}")
    private String uploadDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",            // 登录接口
                        "/uploads/**",       // 上传后的静态资源
                        "/index.html", "/", "/favicon.ico",
                        "/assets/**",        // 前端打包后的静态资源
                        "/error"
                );
    }

    /** 把上传目录映射成静态资源，方便前端直接通过 URL 访问图片 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolute = new File(uploadDir).getAbsolutePath();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolute + File.separator);
    }

    /** 开发阶段前端跑在 5173 端口，这里放开跨域（生产环境前后端同源，用不到） */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
