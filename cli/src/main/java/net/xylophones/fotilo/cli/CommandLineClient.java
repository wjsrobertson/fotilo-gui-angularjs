package net.xylophones.fotilo.cli;

import net.xylophones.fotilo.common.CameraInfo;
import net.xylophones.fotilo.io.JPT3815WCameraControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

@Component
public class CommandLineClient {

    @Autowired
    private PrintWriter consolePrintWriter;

    @Autowired
    private CommandLineArgumentsParser parser;

    @Autowired
    private HelpPrinter helpPrinter;

    public void execute(String[] commandLineArguments) {
        CommandLineOptions commandLineOptions = parser.parseCommandLineArguments(commandLineArguments);

        if (commandLineOptions == null) {
            helpPrinter.printHelp();
            return;
        }

        processCommandLine(commandLineOptions);
    }

    private void processCommandLine(CommandLineOptions options) {
        if (options.isHelp() || (options.getDirection() == null)) {
            helpPrinter.printHelp();
            return;
        } else {
            CameraInfo cameraInfo = getCameraInfo(options);
            try (JPT3815WCameraControl control = new JPT3815WCameraControl(cameraInfo)) {

                if (options.getImage() != null) {
                    control.saveSnapshot(Paths.get(options.getImage()));
                }
                if (options.getSpeed() != null) {
                    control.setPanTiltSpeed(options.getSpeed());
                }
                if (options.getBrightness() != null) {
                    control.setBrightness(options.getBrightness());
                }
                if (options.getContrast() != null) {
                    control.setContrast(options.getContrast());
                }
                if (options.getFps() != null) {
                    control.setFrameRate(options.getFps());
                }
                if (options.getFlipRotation() != null) {
                    control.flip(options.getFlipRotation());
                }
                if (options.getResolution() != null) {
                    control.setResolution(options.getResolution());
                }
                if (options.getStore() != null) {
                    control.storeLocation(options.getStore());
                }
                if (options.getGoTo() != null) {
                    control.gotoLocation(options.getGoTo());
                }
                if (options.getDirection() != null) {
                    control.move(options.getDirection());
                    if (options.getTime() != null) {
                        Integer timeInSeconds = options.getTime();
                        try {
                            Thread.sleep(1000 * timeInSeconds);
                        } catch (InterruptedException e) {
                            consolePrintWriter.println("Interrupted - exiting");
                            return;
                        }

                        control.stopMovement();
                    }
                }
            } catch (IOException ioe) {
                consolePrintWriter.println("Problem - exiting: " + ioe.getMessage());
            }
        }
    }

    private CameraInfo getCameraInfo(CommandLineOptions options) {
        CameraInfo info = new CameraInfo();

        String configFilename = options.getFile();

        if (configFilename == null) {
            info.setUsername(options.getUsername());
            info.setPassword(options.getPassword());
            info.setHost(options.getHost());
            info.setPort(options.getPort());
        }

        return info;
    }
}
