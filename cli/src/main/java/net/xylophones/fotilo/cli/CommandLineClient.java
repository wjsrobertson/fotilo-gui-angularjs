package net.xylophones.fotilo.cli;

import net.xylophones.fotilo.common.CameraInfo;
import net.xylophones.fotilo.common.Direction;
import net.xylophones.fotilo.io.CameraControl;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        if (cmd.hasOption("help") || (! cmd.hasOption("command") && ! cmd.hasOption("interactive"))) {
            printHelp();
            return;
        } else {
            CameraInfo cameraInfo = getCamerainfo(cmd);
            try (CameraControl control = new CameraControl(cameraInfo)) {
                String command = cmd.getOptionValue("command");
                Direction direction = Direction.fromString(command);
                control.move(direction);
                String timeString = cmd.getOptionValue("time");
                Integer timeInSeconds = Integer.valueOf(timeString);
                try {
                    Thread.sleep(1000 * timeInSeconds);
                } catch (InterruptedException e) {
                    consolePrintWriter.println("Interrupted - exiting");
                }
            } catch (IOException ioe) {
                consolePrintWriter.println("Problem - exiting: " + ioe.getMessage());
            }
        }
    }

    private void printHelp() {
        String command = SystemUtils.IS_OS_WINDOWS ? APP_COMMAND_WINDOWS : APP_COMMAND_NON_WINDOWS;
        helpFormatter.printHelp( consolePrintWriter, 80, command, "", options, 4, 8, "" );
    }

    private CameraInfo getCamerainfo(CommandLine cmd) {
        CameraInfo info = new CameraInfo();

        String configFilename = cmd.getOptionValue("file");

        if (configFilename == null) {
            info.setUsername(cmd.getOptionValue("username"));
            info.setPassword(cmd.getOptionValue("password"));
            info.setHost(cmd.getOptionValue("host"));
            info.setPort(Integer.valueOf(cmd.getOptionValue("port"))); // TODO
        }

        return info;
    }

}
