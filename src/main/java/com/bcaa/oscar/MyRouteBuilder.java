package com.bcaa.oscar;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.ShutdownStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class MyRouteBuilder extends RouteBuilder {

    @Value("${transfer.from.broker-uri}")
    public String fromQUri;

    @Value("${transfer.to.broker-uri}")
    public String toQUri;

    @Value("${transfer.from.file-uri}")
    public String fromFileUri;

    @Value("${transfer.to.file-uri}")
    public String toFileUri;

    @Autowired
    ApplicationContext context;

    @Override
    public void configure() throws Exception {
        from(fromQUri)
            .routeId("QUEUE_TO_QUEUE")
            .autoStartup(false)
            .log("${body}")
            .to(toQUri)
            .onCompletion()
            .to("direct:shutdown")
            .end()
        ;

        from(fromFileUri)
                .autoStartup(true)
                .routeId("FILE_TO_QUEUE")
                .split(body().tokenize(System.getProperty("line.separator"))).shareUnitOfWork()
                    .log("${body}")
                    .to(toQUri)
                .end()
                .onCompletion()
                .to("direct:shutdown")
                .end()
        ;

        from(fromQUri)
                .autoStartup(false)
                .routeId("QUEUE_TO_FILE")
                .log("${body}")
                .process(exchange -> {
                    exchange.getMessage().setBody(exchange.getMessage().getBody(String.class) + System.getProperty("line.separator"));
                })
                .to(toFileUri)
        ;
        from("direct:shutdown")
                .log("Shutting down ...")
                .process(exchange -> {
                    exchange.getContext().shutdown();
                    System.exit(0);
                });
    }
}
