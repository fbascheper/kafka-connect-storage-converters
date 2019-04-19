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

    @Override
    public BufferedImage transform(BufferedImage source) {
        BufferedImage convertedImg = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        convertedImg.getGraphics().drawImage(source, 0, 0, Color.BLACK, null);
        convertedImg.getGraphics().dispose();

        BufferedImage resized = ImageSizeTransformer.DEFAULT.transform(convertedImg);
        BufferedImage cropped = ImageCropTransformer.DEFAULT.transform(resized);
        BufferedImage grayscale = GrayScaleTransformer.INSTANCE.transform(cropped);

        return grayscale;

    }

}
