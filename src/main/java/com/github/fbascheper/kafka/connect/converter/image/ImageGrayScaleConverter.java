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

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Grayscale and resizing converter for images using raw byte data.
 * <p>
 * This implementation converts images to grayscale and a fixed width.
 *
 * @author Erik-Berndt Scheper
 * @since 05-11-2018
 */
public class ImageGrayScaleConverter implements Converter, HeaderConverter {

    private static final ConfigDef CONFIG_DEF = ConverterConfig.newConfigDef();

    private static final Logger LOGGER = getLogger(ImageGrayScaleConverter.class);

    private ImageTransformer imageTransformer = new CombinedImageTransformer();

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
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
        try {
            BufferedImage source = ImageIO.read(new ByteArrayInputStream(data));
            BufferedImage transformedImage = imageTransformer.transform(source);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(transformedImage, "jpg", bos);

            return bos.toByteArray();

        } catch (Exception ex) {
            LOGGER.error("Image conversion failure", ex);
            return data;
        }

    }

}
