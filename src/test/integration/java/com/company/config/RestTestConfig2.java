package com.company.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;

@ComponentScan("com.company")
@EnableWebFlux
public class RestTestConfig2 {

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route(RequestPredicates.GET("/"), (request) -> ServerResponse.status(HttpStatus.OK).syncBody("Hello, World"));
    }

    public static void main(String[] args) {
        RouterFunction<?> routerFunction = new AnnotationConfigApplicationContext(RestTestConfig2.class).getBean(RouterFunction.class);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(RouterFunctions.toHttpHandler(routerFunction));
        HttpServer httpServer = HttpServer.create();
//        httpServer.startAndAwait(adapter);
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(1))
                .setReadTimeout(Duration.ofSeconds(1));
    }


}
