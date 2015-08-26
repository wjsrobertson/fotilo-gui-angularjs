package net.xylophones.fotilo.io;

import net.xylophones.fotilo.common.CameraSettings;
import org.junit.Test;

import java.io.InputStream;

import static org.fest.assertions.Assertions.assertThat;

public class TR3818SettingsPageParserTest {

    TR3818SettingsPageParser underTest = new TR3818SettingsPageParser();

    @Test
    public void testParseFromPage() throws Exception {
        InputStream resourceAsStream = JPT3815WSettingsPageParserIntegrationTest.class.getResourceAsStream("/tr3818_get_camera_params.cgi.html");

        CameraSettings cameraSettings = underTest.parseFromPage(resourceAsStream);

        assertThat(cameraSettings.getPanTiltSpeed()).isEqualTo(10);
        assertThat(cameraSettings.getBrightness()).isEqualTo(123);
        assertThat(cameraSettings.getFrameRate()).isEqualTo(15);
        assertThat(cameraSettings.getResolution()).isEqualTo("640x480");
        assertThat(cameraSettings.getContrast()).isEqualTo(128);
        assertThat(cameraSettings.getInfrRedCutEnabled()).isEqualTo(false);
    }

}