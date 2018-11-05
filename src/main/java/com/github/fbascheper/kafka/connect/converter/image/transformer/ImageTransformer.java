package com.github.fbascheper.kafka.connect.converter.image.transformer;

import java.awt.image.BufferedImage;

/**
 * Specification of an image transformer.
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public interface ImageTransformer {

    /**
     * Transformation method.
     *
     * @param source source image
     * @return resulting image after the transformation
     */
    BufferedImage transform(BufferedImage source);
}
