package com.github.fbascheper.image.transformer;

import com.github.fbascheper.image.transformer.util.Scalr;

import java.awt.image.BufferedImage;

/**
 * Transforms the size of a given image by cropping to a width and height.
 *
 * @author Erik-Berndt Scheper
 * @since 15-04-2019
 */
public class ImageCropTransformer implements ImageTransformer {

    public static final ImageCropTransformer DEFAULT = new ImageCropTransformer(165, 200, 50, 50);

    private final int cropLeft;
    private final int cropRight;
    private final int cropTop;
    private final int cropBottom;

    /**
     * Constructor for crop operations, using the given parameters
     *
     * @param cropLeft   pixels to crop from left
     * @param cropRight  pixels to crop from right
     * @param cropTop    pixels to crop from top
     * @param cropBottom pixels to crop from bottom
     */
    public ImageCropTransformer(int cropLeft, int cropRight, int cropTop, int cropBottom) {
        this.cropLeft = cropLeft >= 0 ? cropLeft : 0;
        this.cropRight = cropRight >= 0 ? cropRight : 0;
        this.cropTop = cropTop >= 0 ? cropTop : 0;
        this.cropBottom = cropBottom >= 0 ? cropBottom : 0;
    }

    @Override
    public BufferedImage transform(BufferedImage source) {
        final int x = this.cropLeft;
        final int y = this.cropTop;
        final int width = source.getWidth() - x - this.cropRight;
        final int height = source.getHeight() - y - this.cropBottom;

        if (width >= 0 && height >= 0) {
            return Scalr.crop(source, x, y, width, height);
        } else {
            return Scalr.crop(source, 0, 0, 0, 0);
        }

    }

}
