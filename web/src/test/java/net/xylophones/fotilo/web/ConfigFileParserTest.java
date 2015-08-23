package net.xylophones.fotilo.web;

import net.xylophones.fotilo.web.configfile.ConfigFileParser;
import net.xylophones.fotilo.web.configfile.model.CameraConfig;
import net.xylophones.fotilo.web.configfile.model.ConfigFile;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ConfigFileParserTest {

    @InjectMocks
    private ConfigFileParser underTest;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        ClassPathResource validFileResource
                = new ClassPathResource("net/xylophones/fotilo/web/ConfigFileParserTest_validConfigFile.json");
        File configFile = temporaryFolder.newFile();
        FileUtils.copyInputStreamToFile(validFileResource.getInputStream(), configFile);

        System.setProperty("fotilo.config.file", configFile.getAbsolutePath());

    }

    @Test
    public void testGetConfigFileFromSystemProperty() {
        ConfigFile configFileFromSystemProperty = underTest.getConfigFileFromSystemProperty();

        CameraConfig expectedCameraConfig = getExpectedCameraConfig();

        assertThat(configFileFromSystemProperty.getCameras())
                .hasSize(1)
                .contains(expectedCameraConfig);
    }

    private CameraConfig getExpectedCameraConfig() {
        CameraConfig expectedCameraConfig = new CameraConfig();

        expectedCameraConfig.setId("camera");
        expectedCameraConfig.setHost("example.com");
        expectedCameraConfig.setPassword("test");
        expectedCameraConfig.setPort(80);
        expectedCameraConfig.setUsername("test");
        expectedCameraConfig.setType("JPT3815W");

        return expectedCameraConfig;
    }
}