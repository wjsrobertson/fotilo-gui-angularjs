package net.xylophones.fotilo.cli;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Component
public class CommandLineClient {

    private static final String APP_COMMAND_WINDOWS = "fotilo.bat";

    private static final String APP_COMMAND_NON_WINDOWS = "fotilo.sh";

    @Autowired
    private Options options;

    @Autowired
    private PrintWriter consolePrintWriter;

    @Autowired
    private CommandLineParser commandLineParser;

    @Autowired
    private HelpFormatter helpFormatter;

    public void execute(String[] commandLineArguments) {
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options, commandLineArguments);
            processCommandLine(commandLine);

        } catch (ParseException e) {
            consolePrintWriter.println(e.getMessage());
            printHelp();
        }
    }

    private void processCommandLine(CommandLine cmd) {
        if (cmd.hasOption("h") || (! cmd.hasOption("c") && ! cmd.hasOption("i"))) {
            printHelp();
            return;
        } else {
            CameraInfo cameraInfo = getCamerainfo(cmd);

        }
    }

    private void printHelp() {
        String command = SystemUtils.IS_OS_WINDOWS ? APP_COMMAND_WINDOWS : APP_COMMAND_NON_WINDOWS;
        helpFormatter.printHelp( consolePrintWriter, 80, command, "", options, 4, 8, "" );
    }

    private CameraInfo getCamerainfo(CommandLine cmd) {
        String configFilename = cmd.getOptionValue("f");

        return null;
    }

}
