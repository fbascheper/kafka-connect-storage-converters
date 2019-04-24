package com.github.fbascheper.image.transformer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple application that transforms an image into a grayscale equivalent using the given width.
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public class CombinedImageTransformerApplication {

    private static final Integer RESIZE_TARGET_WIDTH = 1024;
    private static final Integer RESIZE_TARGET_HEIGHT = -1;

    private static final Integer CROP_LEFT = 165;
    private static final Integer CROP_RIGHT = 200;
    private static final Integer CROP_TOP = 50;
    private static final Integer CROP_BOTTOM = 50;

    /**
     * Main program to traverse a directory and transform all images inside it.
     * <ul>
     * <li>Source directory</li>
     * <li>Destination directory</li>
     * </ul>
     *
     * @param args arguments
     * @throws IOException if the files cannot be read
     */
    public static void main(final String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected exactly two arguments (source and destination)");
        }

        String sourceDirectory = args[0];
        String destDirectory = args[1];

        checkDirectory(sourceDirectory);
        checkDirectory(destDirectory);

        CombinedImageTransformer imageTransformer = new CombinedImageTransformer(RESIZE_TARGET_WIDTH, RESIZE_TARGET_HEIGHT,
                CROP_LEFT, CROP_RIGHT, CROP_TOP, CROP_BOTTOM);

        Path sourcePath = Paths.get(sourceDirectory);
        Path destPath = Paths.get(destDirectory);

        Files.walk(sourcePath)
                .filter(p -> p.toString().endsWith(".jpg"))
                .distinct()
                .forEach(p -> transform(p, sourcePath, destPath, imageTransformer)
                );

    }

    private static void transform(Path imageSrcPath, Path sourcePath, Path destPath, ImageTransformer transformer) {
        Path imgRelative = sourcePath.relativize(imageSrcPath);
        Path imgDestPath = destPath.resolve(imgRelative);

        try {

            boolean res = imgDestPath.getParent().toFile().mkdirs();
            BufferedImage source = ImageIO.read(imageSrcPath.toFile());
            BufferedImage dest = transformer.transform(source);
            ImageIO.write(dest, "jpg", imgDestPath.toFile());

        } catch (IOException e) {
            throw new IllegalStateException("Could not sanitize image", e);
        }
    }

    private static void checkDirectory(String name) {
        File file = new File(name);
        if (!file.exists() && !file.isDirectory()) {
            throw new IllegalArgumentException("Directory '" + name + "' does not exist.");
        }

    }

}
