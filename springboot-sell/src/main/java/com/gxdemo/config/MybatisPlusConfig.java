package com.gxdemo.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return  new PaginationInterceptor();
    }

//    @Bean
//    @Profile({"dev","test"})
//    public PerformanceMonitorInterceptor performanceMonitorInterceptor(){
//       PerformanceMonitorInterceptor performanceMonitorInterceptor = new PerformanceMonitorInterceptor();
//       return performanceMonitorInterceptor;
//    }

}
