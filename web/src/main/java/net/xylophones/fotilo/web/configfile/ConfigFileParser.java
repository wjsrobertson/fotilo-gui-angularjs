package net.xylophones.fotilo.web.configfile;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.xylophones.fotilo.web.configfile.model.ConfigFile;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ConfigFileParser {

    private static final String CONFIG_FILE_PROPERTY_NAME = "fotilo.config.file";

    public ConfigFile getConfigFileFromSystemProperty() {
        String filePath = System.getProperty(CONFIG_FILE_PROPERTY_NAME);
        if (filePath == null) {
            throw new ConfigFileException("Config file system property not found - " + CONFIG_FILE_PROPERTY_NAME);
        }

        Path path = Paths.get(filePath);
        return parseConfigFile(path);
    }

    private ConfigFile parseConfigFile(Path path) {
        try {
            FileReader fileReader = new FileReader(path.toFile());
            return new Gson().fromJson(fileReader, ConfigFile.class);
        } catch (FileNotFoundException | JsonIOException | JsonSyntaxException e) {
            throw new ConfigFileException(e);
        }
    }
}
