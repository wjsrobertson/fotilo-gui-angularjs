package net.xylophones.fotilo.web.configfile;

import net.xylophones.fotilo.web.configfile.model.ConfigFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FotiloConfiguration {

    @Autowired
    private ConfigFileParser configFileParser;

    @Bean
    public ConfigFile createConfigFile() {
        return configFileParser.getConfigFileFromSystemProperty();
    }

}
