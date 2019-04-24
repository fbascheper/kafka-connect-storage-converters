package com.github.fbascheper.kafka.connect.converter.image;

import com.github.fbascheper.image.transformer.CombinedImageTransformer;
import com.github.fbascheper.image.transformer.ImageTransformer;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.storage.Converter;
import org.apache.kafka.connect.storage.ConverterConfig;
import org.apache.kafka.connect.storage.HeaderConverter;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * A Kafka-connect converter for images using raw byte data that performs the following operations:
 * <ul>
 * <li>Resize image to target width / height, using {@link com.github.fbascheper.image.transformer.ImageSizeTransformer}</li>
 * <li>Crop image, using {@link com.github.fbascheper.image.transformer.ImageCropTransformer}</li>
 * <li>Convert to GrayScale, using {@link com.github.fbascheper.image.transformer.GrayScaleTransformer}</li>
 * </ul>
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public class ImageGrayScaleConverter implements Converter, HeaderConverter {

    private static final Logger LOGGER = getLogger(ImageGrayScaleConverter.class);

    private static final String CONFIG_NAME_RESIZE_TARGET_WIDTH = "resizeTargetWidth";
    private static final String CONFIG_NAME_RESIZE_TARGET_WIDTH_DOC = "Target width of the image";

    private static final String CONFIG_NAME_RESIZE_TARGET_HEIGHT = "resizeTargetHeight";
    private static final String CONFIG_NAME_RESIZE_TARGET_HEIGHT_DOC = "Target height of the image; set to -1 (or zero) to scale height proportionally to width";

    private static final String CONFIG_NAME_CROP_LEFT = "cropLeft";
    private static final String CONFIG_NAME_CROP_LEFT_DOC = "Number of pixels to crop from left hand side of the image";

    private static final String CONFIG_NAME_CROP_RIGHT = "cropRight";
    private static final String CONFIG_NAME_CROP_RIGHT_DOC = "Number of pixels to crop from right hand side of the image";

    private static final String CONFIG_NAME_CROP_TOP = "cropTop";
    private static final String CONFIG_NAME_CROP_TOP_DOC = "Number of pixels to crop from top of the image";

    private static final String CONFIG_NAME_CROP_BOTTOM = "cropBottom";
    private static final String CONFIG_NAME_CROP_BOTTOM_DOC = "Number of pixels to crop from bottom of the image";


    private static final ConfigDef CONFIG_DEF = ConverterConfig.newConfigDef()
            .define(CONFIG_NAME_RESIZE_TARGET_WIDTH, ConfigDef.Type.INT, ConfigDef.NO_DEFAULT_VALUE, ConfigDef.Range.atLeast(1), ConfigDef.Importance.HIGH, CONFIG_NAME_RESIZE_TARGET_WIDTH_DOC)
            .define(CONFIG_NAME_RESIZE_TARGET_HEIGHT, ConfigDef.Type.INT, -1, ConfigDef.Range.atLeast(-1), ConfigDef.Importance.HIGH, CONFIG_NAME_RESIZE_TARGET_HEIGHT_DOC)
            .define(CONFIG_NAME_CROP_LEFT, ConfigDef.Type.INT, 0, ConfigDef.Range.atLeast(0), ConfigDef.Importance.HIGH, CONFIG_NAME_CROP_LEFT_DOC)
            .define(CONFIG_NAME_CROP_RIGHT, ConfigDef.Type.INT, 0, ConfigDef.Range.atLeast(0), ConfigDef.Importance.HIGH, CONFIG_NAME_CROP_RIGHT_DOC)
            .define(CONFIG_NAME_CROP_TOP, ConfigDef.Type.INT, 0, ConfigDef.Range.atLeast(0), ConfigDef.Importance.HIGH, CONFIG_NAME_CROP_TOP_DOC)
            .define(CONFIG_NAME_CROP_BOTTOM, ConfigDef.Type.INT, 0, ConfigDef.Range.atLeast(0), ConfigDef.Importance.HIGH, CONFIG_NAME_CROP_BOTTOM_DOC);

    private ImageTransformer imageTransformer = null;

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        if (!isKey) {
            Integer resizeTargetWidth = (Integer) configs.get(CONFIG_NAME_RESIZE_TARGET_WIDTH);
            Integer resizeTargetHeight = (Integer) configs.get(CONFIG_NAME_RESIZE_TARGET_HEIGHT);
            Integer cropLeft = (Integer) configs.get(CONFIG_NAME_CROP_LEFT);
            Integer cropRight = (Integer) configs.get(CONFIG_NAME_CROP_RIGHT);
            Integer cropTop = (Integer) configs.get(CONFIG_NAME_CROP_TOP);
            Integer cropBottom = (Integer) configs.get(CONFIG_NAME_CROP_BOTTOM);

            this.imageTransformer = new CombinedImageTransformer(resizeTargetWidth, resizeTargetHeight, cropLeft, cropRight, cropTop, cropBottom);
        }
    }

    @Override
    public byte[] fromConnectData(String topic, Schema schema, Object value) {
        if (schema != null && schema.type() != Schema.Type.BYTES)
            throw new DataException("Invalid schema type for ImageGrayScaleConverter: " + schema.type().toString());

        if (value != null && !(value instanceof byte[]))
            throw new DataException("ImageGrayScaleConverter is not compatible with objects of type " + value.getClass());

        return convert((byte[]) value);
    }

    @Override
    public SchemaAndValue toConnectData(String topic, byte[] value) {
        return new SchemaAndValue(Schema.OPTIONAL_BYTES_SCHEMA, value);
    }

    @Override
    public byte[] fromConnectHeader(String topic, String headerKey, Schema schema, Object value) {
        return fromConnectData(topic, schema, value);
    }

    @Override
    public SchemaAndValue toConnectHeader(String topic, String headerKey, byte[] value) {
        return toConnectData(topic, value);
    }

    @Override
    public void close() {
        // do nothing
    }

    private byte[] convert(byte[] data) {
        Objects.requireNonNull(this.imageTransformer);

        try {
            BufferedImage source = ImageIO.read(new ByteArrayInputStream(data));
            BufferedImage transformedImage = this.imageTransformer.transform(source);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(transformedImage, "jpg", bos);

            return bos.toByteArray();

        } catch (Exception ex) {
            LOGGER.error("Image conversion failure", ex);
            return data;
        }

    }

}
