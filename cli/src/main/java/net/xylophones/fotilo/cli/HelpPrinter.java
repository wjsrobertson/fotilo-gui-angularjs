package net.xylophones.fotilo.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Component
public class HelpPrinter {

    private static final String APP_COMMAND_WINDOWS = "fotilo.bat";

    private static final String APP_COMMAND_NON_WINDOWS = "fotilo.sh";

    @Autowired
    private Options options;

    @Autowired
    private PrintWriter consolePrintWriter;

    @Autowired
    private HelpFormatter helpFormatter;

    public void printHelp() {
        String command = SystemUtils.IS_OS_WINDOWS ? APP_COMMAND_WINDOWS : APP_COMMAND_NON_WINDOWS;
        helpFormatter.printHelp(consolePrintWriter, 80, command, "", options, 4, 8, "");
    }
}
