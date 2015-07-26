package net.xylophones.fotilo.cli;

import net.xylophones.fotilo.common.Direction;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static net.xylophones.fotilo.cli.ProgramArguments.*;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class CommandLineArgumentsParser {

    @Autowired
    private CommandLineParser commandLineParser;

    @Autowired
    private Options options;

    public CommandLineOptions x(String[] commandLineArguments) {
        CommandLine commandLine = null;

        try {
            commandLine = commandLineParser.parse(options, commandLineArguments);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CommandLineOptions options = new CommandLineOptions();

        String help = commandLine.getOptionValue(HELP.getArgName());
        options.setHelp(resolveBoolean(help));

        String interactive = commandLine.getOptionValue(INTERACTIVE.getArgName());
        options.setInteractive(resolveBoolean(interactive));

        String file = commandLine.getOptionValue(FILE.getArgName());
        options.setFile(resolveString(file));

        String direction = commandLine.getOptionValue(DIRECTION.getArgName());
        options.setDirection(Direction.fromString(direction));

        String time = commandLine.getOptionValue(TIME.getArgName());
        String host = commandLine.getOptionValue(HOST.getArgName());
        String port = commandLine.getOptionValue(PORT.getArgName());
        String username = commandLine.getOptionValue(USERNAME.getArgName());
        String password = commandLine.getOptionValue(PASSWORD.getArgName());
        String image = commandLine.getOptionValue(IMAGE.getArgName());
        String speed = commandLine.getOptionValue(SPEED.getArgName());
        String brightness = commandLine.getOptionValue(BRIGHTNESS.getArgName());
        String contrast = commandLine.getOptionValue(CONTRAST.getArgName());
        String resolution = commandLine.getOptionValue(RESOLUTION.getArgName());
        String flip = commandLine.getOptionValue(FLIP.getArgName());
        String storeLocation = commandLine.getOptionValue(STORE_LOCATION.getArgName());
        String gotoLocation = commandLine.getOptionValue(GOTO_LOCATION.getArgName());
        String frameRage = commandLine.getOptionValue(FRAMES_PER_SECOND.getArgName());

        return options;
    }

    private Integer resolveInt(String toResolve) {
        if (toResolve != null) {
            return Integer.valueOf(toResolve);
        }

        return null;
    }

    private Boolean resolveBoolean(String toResolve) {
        if (toResolve != null) {

        }

        return null;
    }

    private String resolveString(String toResolve) {
        if (! isEmpty(toResolve)) {
            return toResolve;
        }

        return null;
    }



}
