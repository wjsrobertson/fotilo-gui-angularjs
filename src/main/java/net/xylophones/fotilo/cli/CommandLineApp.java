package net.xylophones.fotilo.cli;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CommandLineApp {

    private static final String APP_CONTEXT_XML_FILE = "appCtx-fotilo-cli.xml";

    public static void main(String[] args) {
        ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(APP_CONTEXT_XML_FILE);
        CommandLineClient client = appCtx.getBean(CommandLineClient.class);

        client.execute(args);
    }

}

