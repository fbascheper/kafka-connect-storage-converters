# Kafka-connect-storage-converters 

> Simple storage converters for  [Kafka Connect](https://docs.confluent.io/current/connect/index.html)

[![Build Status](https://travis-ci.com/fbascheper/kafka-connect-storage-converters.svg?branch=develop)](https://travis-ci.com/fbascheper/kafka-connect-storage-converters)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fbascheper/kafka-connect-storage-converters/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.fbascheper/kafka-connect-storage-converters)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/fbascheper/kafka-connect-storage-converters/master/LICENSE.txt)

***

### Contents
This repository contains two storage converters for Kafka Connect: 

* A no-op converter for byte arrays:
  `com.github.fbascheper.kafka.connect.converter.bytearray.ByteArrayConverter`
* A combined grayscale, resize and crop converter for images
  `com.github.fbascheper.kafka.connect.converter.image.ImageGrayScaleConverter`

### Usage
Copy the `kafka-connect-storage-converters` jar to your Kafka Connect instance and restart Kafka Connect.

Converters can be specified on a per-connector basis.

To use the image converter in Kafka Connect, specify the converter as your value converter and specify the
values for resize and crop operations.

Example Kafka Connect FTP source:
```
{
  "name": "example-connect-ftp-source",
  "config": {
    "connector.class": "com.datamountaineer.streamreactor.connect.ftp.source.FtpSourceConnector",
    "tasks.max": 1,
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "com.github.fbascheper.kafka.connect.converter.image.ImageGrayScaleConverter",
    "value.converter.resizeTargetWidth": 1024,
    "value.converter.resizeTargetHeight": -1,
    "value.converter.cropLeft": 165,
    "value.converter.cropRight": 200,
    "value.converter.cropTop": 50,
    "value.converter.cropBottom": 50,
    "connect.ftp.address": "vsftpd.local:21",
    "connect.ftp.user": "photo",
    "connect.ftp.password": "upload",
    "connect.ftp.refresh": "PT1M",
    "connect.ftp.file.maxage": "P14D",
    "connect.ftp.keystyle": "string",
    "connect.ftp.monitor.update": "/path/for/update/:dest-ftp-topic",
    "connect.ftp.sourcerecordconverter": "com.datamountaineer.streamreactor.connect.ftp.source.NopSourceRecordConverter",
    "connect.ftp.fileconverter": "com.datamountaineer.streamreactor.connect.ftp.source.SimpleFileConverter",
    "connect.ftp.filter": ".*",
    "connect.ftp.protocol": "sftp"
  }
```
