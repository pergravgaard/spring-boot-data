package com.company.config;

import com.company.security.FirstFilter;
import com.company.security.LastFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.IOException;
import java.util.HashMap;

@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<FirstFilter> firstFilter() {
        FilterRegistrationBean<FirstFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new FirstFilter());
        filterRegistrationBean.addUrlPatterns("/", "/*");
        filterRegistrationBean.setInitParameters(new HashMap<>());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<LastFilter> lastFilter() {
        FilterRegistrationBean<LastFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LastFilter());
        filterRegistrationBean.addUrlPatterns("/", "/*");
        filterRegistrationBean.setInitParameters(new HashMap<>());
        filterRegistrationBean.setOrder(100);
        return filterRegistrationBean;
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/static/");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");

        /* ****** Swagger UI begin ****** */

        registry.addResourceHandler("/swagger-ui.html") // available at http://localhost:8080/swagger-ui.html
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        /* ****** Swagger UI end ****** */

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/")
//                .setCachePeriod(0)
//                .resourceChain(false)
//                .addResolver(new IndexHtmlResolver());

    }

    private static class IndexHtmlResolver extends PathResourceResolver {

        private final Resource indexResource = new ClassPathResource("/static/index.html");

        @Override
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            return indexResource;
        }

    }

}
