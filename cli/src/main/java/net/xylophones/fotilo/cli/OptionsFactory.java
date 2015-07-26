package net.xylophones.fotilo.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.context.annotation.Bean;

public class OptionsFactory {

    public Options createOptions() {
        Options options = new Options();

        for (ProgramArguments argument : ProgramArguments.values()) {
            Option option = createOption(argument.getArgName(), argument.getDescription(), argument.hasArgument());
            options.addOption(option);
        }

        return options;
    }

    private Option createOption(String name, String description, boolean hasArgument) {
        return Option.builder()
                .hasArg(hasArgument)
                .argName(name)
                .longOpt(name)
                .desc(description)
                .build();
    }
}
