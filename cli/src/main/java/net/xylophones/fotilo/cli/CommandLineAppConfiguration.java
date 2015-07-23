package net.xylophones.fotilo.cli;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PrintWriter;

@Configuration
public class CommandLineAppConfiguration {

    @Bean
    public Options options() {
        return new OptionsFactory().createOptions();
    }

    @Bean
    public PrintWriter consolePrintWriter() {
        return System.console().writer();
    }

    @Bean
    public CommandLineParser commandLineParser() {
        return new DefaultParser();
    }

    @Bean
    public HelpFormatter helpFormatter() {
        return new HelpFormatter();
    }

}