package net.xylophones.fotilo.cli;

import net.xylophones.fotilo.common.Direction;
import net.xylophones.fotilo.common.Rotation;
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

    public CommandLineOptions parseCommandLineArguments(String[] args) {
        CommandLine commandLine;

        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            return null;
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
        options.setTime(resolveInt(time));

        String host = commandLine.getOptionValue(HOST.getArgName());
        options.setHost(resolveString(host));

        String port = commandLine.getOptionValue(PORT.getArgName());
        options.setPort(resolveInt(port));

        String username = commandLine.getOptionValue(USERNAME.getArgName());
        options.setUsername(resolveString(username));

        String password = commandLine.getOptionValue(PASSWORD.getArgName());
        options.setPassword(resolveString(password));

        String image = commandLine.getOptionValue(IMAGE.getArgName());
        options.setImage(resolveString(image));

        String speed = commandLine.getOptionValue(SPEED.getArgName());
        options.setSpeed(resolveInt(speed));

        String brightness = commandLine.getOptionValue(BRIGHTNESS.getArgName());
        options.setBrightness(resolveInt(brightness));

        String contrast = commandLine.getOptionValue(CONTRAST.getArgName());
        options.setContrast(resolveInt(contrast));

        String resolution = commandLine.getOptionValue(RESOLUTION.getArgName());
        options.setResolution(resolveString(resolution));

        String flip = commandLine.getOptionValue(FLIP.getArgName());
        options.setFlipRotation(Rotation.fromString(flip));

        String storeLocation = commandLine.getOptionValue(STORE_LOCATION.getArgName());
        options.setStore(resolveInt(storeLocation));

        String gotoLocation = commandLine.getOptionValue(GOTO_LOCATION.getArgName());
        options.setGoTo(resolveInt(gotoLocation));

        String frameRage = commandLine.getOptionValue(FRAMES_PER_SECOND.getArgName());
        options.setFps(resolveInt(frameRage));

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
            return Boolean.valueOf(toResolve);
        }

        return false;
    }

    private String resolveString(String toResolve) {
        if (! isEmpty(toResolve)) {
            return toResolve;
        }

        return null;
    }
}
