package com.github.fbascheper.image.transformer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Run a set of transformers together on a source image to produce a target image.
 *
 * @author Erik-Berndt Scheper
 * @since 15-04-2019
 */
public class CombinedImageTransformer implements ImageTransformer {

    private final ImageSizeTransformer imageSizeTransformer;
    private final ImageCropTransformer imageCropTransformer;
    private final GrayScaleTransformer grayScaleTransformer = GrayScaleTransformer.INSTANCE;

    /**
     * Initializing constructor.
     *
     * @param resizeTargetWidth  target width for resize transformer
     * @param resizeTargetHeight target width for resize transformer
     * @param cropLeft           pixels to crop from left
     * @param cropRight          pixels to crop from right
     * @param cropTop            pixels to crop from top
     * @param cropBottom         pixels to crop from bottol
     */
    public CombinedImageTransformer(Integer resizeTargetWidth, Integer resizeTargetHeight, Integer cropLeft,
                                    Integer cropRight, Integer cropTop, Integer cropBottom) {
        if (resizeTargetWidth <= 0) {
            throw new IllegalArgumentException("resizeTargetWidth should be at least 1");
        }

        imageSizeTransformer = new ImageSizeTransformer(resizeTargetWidth, resizeTargetHeight);
        imageCropTransformer = new ImageCropTransformer(cropLeft, cropRight, cropTop, cropBottom);
    }

    @Override
    public BufferedImage transform(BufferedImage source) {
        BufferedImage convertedImg = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        convertedImg.getGraphics().drawImage(source, 0, 0, Color.BLACK, null);
        convertedImg.getGraphics().dispose();

        BufferedImage resized = this.imageSizeTransformer.transform(convertedImg);
        BufferedImage cropped = this.imageCropTransformer.transform(resized);
        BufferedImage grayscale = this.grayScaleTransformer.transform(cropped);

        return grayscale;

    }

}
