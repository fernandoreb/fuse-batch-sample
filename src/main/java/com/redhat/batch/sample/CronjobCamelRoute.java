package com.redhat.batch.sample;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CronjobCamelRoute extends RouteBuilder {

    @Autowired
	private SQLProcessor sqlProcessor;
    
    @Override
    public void configure() throws Exception {
        from("quartz2://execucaoAutomatica?cron={{scheduler.cron.expression}}")
            .to("direct:processar").id("processar");
        
        from("direct:processar").routeId("processar-dados")
            .log("job camel sample started")
            .to("{{api.random}}/random")
				.routeId("get-string-random")
            .process(sqlProcessor).id("sql-processor")
            .to("jdbc:dataSource").id("jdbc-endpoint")
            .log("job camel sample finished");
        
    }

}