package com.github.fbascheper.kafka.connect.converter.image.transformer.util;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

/**
 * Test images.
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public enum TestImage {

    /**
     * Gray scale image
     */
    GRAYSCALE_IMAGE("images/MDAlarm_20180313-064350.jpg"),

    /**
     * Color image
     */
    COLOR_IMAGE("images/MDAlarm_20180313-082002.jpg"),

    /**
     * Mask devil image
     */
    MASK_DEVIL_IMAGE("images/devil.png");

    private final String location;

    TestImage(String location) {
        this.location = location;
    }

    public InputStream toInputStream() {
        InputStream result = TestImage.class.getClassLoader().getResourceAsStream(location);
        Objects.requireNonNull(result);
        return result;
    }

    public File toTempOutputFile(String prefix) {
        URL url = TestImage.class.getClassLoader().getResource(location);
        File f;
        try {
            f = new File(url.toURI());
        } catch (URISyntaxException e) {
            f = new File(url.getPath());
        }
        return new File(new File(f.getParentFile(), "tmp"), prefix + f.getName());
    }
}
