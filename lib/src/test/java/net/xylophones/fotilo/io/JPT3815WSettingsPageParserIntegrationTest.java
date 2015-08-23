package net.xylophones.fotilo.io;

import junit.framework.TestCase;
import net.xylophones.fotilo.common.CameraSettings;
import org.junit.Test;

import java.io.InputStream;

import static org.fest.assertions.Assertions.assertThat;

public class JPT3815WSettingsPageParserIntegrationTest {

    JPT3815WSettingsPageParser underTest = new JPT3815WSettingsPageParser();

    @Test
    public void testParseFromPage() throws Exception {
        InputStream resourceAsStream = JPT3815WSettingsPageParserIntegrationTest.class.getResourceAsStream("/jpt3815w_livesp.asp.html.html");

        CameraSettings cameraSettings = underTest.parseFromPage(resourceAsStream);

        assertThat(cameraSettings.getPanTiltSpeed()).isEqualTo(0);
        assertThat(cameraSettings.getBrightness()).isEqualTo(4);
        assertThat(cameraSettings.getFrameRate()).isEqualTo(30);
        assertThat(cameraSettings.getResolution()).isEqualTo("640x480");
        assertThat(cameraSettings.getContrast()).isEqualTo(2);
    }
}