package net.xylophones.fotilo.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.context.annotation.Bean;

public class OptionsFactory {

    @Bean
    public Options createOptions() {
        Option help = Option.builder()
                .hasArg(false)
                .argName("h")
                .longOpt("help")
                .desc("print help and exit")
                .build();

        Option file = Option.builder()
                .hasArg()
                .argName("f")
                .longOpt("file")
                .desc("config properties file with camera information. This is an alternative to specifying user, pass, host, port at the command line.")
                .build();

        Option command = Option.builder()
                .hasArg()
                .argName("c")
                .longOpt("command")
                .desc("command to execute")
                .build();

        Option time = Option.builder()
                .hasArg()
                .argName("t")
                .longOpt("time")
                .desc("time to execute command for. Used in conjunction with command (c) argument")
                .build();

        Option interactive = Option.builder()
                .hasArg(false)
                .argName("i")
                .longOpt("interactive")
                .desc("run in interactive mode")
                .build();

        Option host = Option.builder()
                .hasArg()
                .argName("H")
                .longOpt("host")
                .desc("host/IP of camera")
                .build();

        Option port = Option.builder()
                .hasArg()
                .argName("p") // lower case p
                .longOpt("port")
                .desc("port of camera")
                .build();

        Option username = Option.builder()
                .hasArg()
                .argName("u")
                .longOpt("username")
                .desc("username for camera authentication")
                .build();

        Option password = Option.builder()
                .hasArg()
                .argName("P") // upper case p
                .longOpt("password")
                .desc("username for camera authentication")
                .build();

        return buildOptions(help, file, command, time, interactive, host, port, username, password);
    }

    private Options buildOptions(Option... optionsToAdd) {
        Options options = new Options();

        for (Option option : optionsToAdd) {
            options.addOption(option);
        }

        return options;
    }

}
