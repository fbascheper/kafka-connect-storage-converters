package com.github.fbascheper.image.transformer;

import com.github.fbascheper.image.transformer.util.Scalr;

import java.awt.image.BufferedImage;

/**
 * Transforms the size of a given image.
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public class ImageSizeTransformer implements ImageTransformer {

    public static final ImageSizeTransformer DEFAULT = new ImageSizeTransformer(1024);

    private final int targetWidth;
    private final Integer targetHeight;

    public ImageSizeTransformer(int targetWidth) {
        if (targetWidth == 0) {
            throw new IllegalArgumentException("Target width should not be zero");
        }

        this.targetWidth = targetWidth;
        this.targetHeight = null;
    }

    public ImageSizeTransformer(int targetWidth, int targetHeight) {
        if (targetWidth == 0) {
            throw new IllegalArgumentException("Target width should not be zero");
        }
        if (targetHeight == 0) {
            throw new IllegalArgumentException("Target height should not be zero");
        }
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
    }

    @Override
    public BufferedImage transform(BufferedImage source) {
        final int height;

        if (this.targetHeight == null) {
            height = (int) Math.round((((double) targetWidth) * source.getHeight()) / source.getWidth());
        } else {
            height = this.targetHeight;
        }

        return Scalr.resize(source, Scalr.Method.ULTRA_QUALITY, targetWidth, height);
    }

}
