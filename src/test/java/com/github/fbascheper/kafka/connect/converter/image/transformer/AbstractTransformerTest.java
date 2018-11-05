package com.github.fbascheper.kafka.connect.converter.image.transformer;

import com.github.fbascheper.kafka.connect.converter.image.transformer.util.TestImage;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Objects;

/**
 * Abstract test class for a {@link ImageTransformer}.
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public abstract class AbstractTransformerTest {

    private final ImageTransformer transformer;
    private final String prefix;

    public AbstractTransformerTest(ImageTransformer transformer, String prefix) {
        this.transformer = transformer;
        this.prefix = prefix;
    }

    @Test
    public void transformGrayScale() throws Exception {
        transform(TestImage.GRAYSCALE_IMAGE);
    }

    @Test
    public void transformColor() throws Exception {
        transform(TestImage.COLOR_IMAGE);
    }

    private void transform(TestImage testImage) throws Exception {
        InputStream inputStream = testImage.toInputStream();
        Objects.requireNonNull(inputStream);
        BufferedImage image = ImageIO.read(inputStream);
        BufferedImage result = transformer.transform(image);

        ImageIO.write(result, "jpg", testImage.toTempOutputFile(prefix));

    }

}
