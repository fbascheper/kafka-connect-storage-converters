package com.github.fbascheper.image.transformer;

/**
 * Test class for a {@link GrayScaleTransformer}
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public class GrayScaleTransformerTest extends AbstractTransformerTest {

    public GrayScaleTransformerTest() {
        super(new GrayScaleTransformer(), "grayscale-");
    }

}
