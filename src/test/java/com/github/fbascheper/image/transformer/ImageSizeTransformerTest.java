package com.github.fbascheper.image.transformer;

/**
 * Test class for {@link ImageSizeTransformer}
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public class ImageSizeTransformerTest extends AbstractTransformerTest {

    public ImageSizeTransformerTest() {
        super(new ImageSizeTransformer(320), "resize-");
    }
}
