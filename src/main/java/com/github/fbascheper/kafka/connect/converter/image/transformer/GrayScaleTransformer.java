package com.github.fbascheper.kafka.connect.converter.image.transformer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Transforms an image into its grayscale equivalent.
 *
 * @author Erik-Berndt Scheper
 * @see <a href="https://en.wikipedia.org/wiki/Grayscale">GrayScale conversion in WikiPedia</a>
 * @since 05-11-2018
 */
public class GrayScaleTransformer implements ImageTransformer {

    public static final GrayScaleTransformer INSTANCE = new GrayScaleTransformer();

    @Override
    public BufferedImage transform(BufferedImage source) {
        int width = source.getWidth();
        int height = source.getHeight();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(source.getRGB(j, i));

                if (c.getRed() != c.getGreen() || c.getGreen() != c.getBlue()) {
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);
                    Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);

                    source.setRGB(j, i, newColor.getRGB());
                }

            }
        }

        return source;
    }


}
