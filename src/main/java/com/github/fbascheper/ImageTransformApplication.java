package com.github.fbascheper;

import com.github.fbascheper.kafka.connect.converter.image.transformer.GrayScaleTransformer;
import com.github.fbascheper.kafka.connect.converter.image.transformer.ImageSizeTransformer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Simple application that transforms an image into a grayscale equivalent using the given width.
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public class ImageTransformApplication {

    /**
     * Main program transforming an image to its grayscale equivalent, resized to 320 pixels wide.
     * <ul>
     * <li>Source file</li>
     * <li>Destination file</li>
     * </ul>
     *
     * @param args arguments
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected exactly two arguments (source and destination)");
        }

        String sourceFileName = args[0];
        String destFileName = args[1];

        File input = new File(sourceFileName);

        if (!input.exists()) {
            throw new IllegalArgumentException("Source file '" + sourceFileName + "' does not exist.");
        }

        BufferedImage source = ImageIO.read(input);
        BufferedImage resized = ImageSizeTransformer.RESIZE_WIDTH_320.transform(source);
        BufferedImage grayscale = GrayScaleTransformer.INSTANCE.transform(resized);

        ImageIO.write(grayscale, "jpg", new File(destFileName));
    }
}
