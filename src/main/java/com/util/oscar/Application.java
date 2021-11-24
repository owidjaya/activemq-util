package com.util.oscar;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);
    private enum Commands{
       FILE_TO_QUEUE,
       QUEUE_TO_FILE,
       QUEUE_TO_QUEUE
    }

    @Autowired
    private CamelContext context;

    public void setContext(CamelContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("EXECUTING : command line runner");
        try {
            Commands aCommand = Commands.valueOf(args[0]);
            var template = context.createProducerTemplate();
            switch(aCommand) {
                case QUEUE_TO_FILE:
                    LOG.info("Running " + Commands.QUEUE_TO_FILE.toString());
                    template.sendBody("controlbus:route?routeId="+Commands.QUEUE_TO_FILE.toString()+"&action=start", null);
                    break;
                case FILE_TO_QUEUE:
                    LOG.info("Running " + Commands.FILE_TO_QUEUE.toString());
                    template.sendBody("controlbus:route?routeId="+Commands.FILE_TO_QUEUE.toString()+"&action=start", null);
                    break;
                case QUEUE_TO_QUEUE:
                    LOG.info("Running " + Commands.QUEUE_TO_QUEUE.toString());
                    template.sendBody("controlbus:route?routeId="+Commands.QUEUE_TO_QUEUE.toString()+"&action=start", null);
                    break;
                default:
                    LOG.info("Not a recognized command");

            }
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
            LOG.error("Command not accepted");
        }
    }
}