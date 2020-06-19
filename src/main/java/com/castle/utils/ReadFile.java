package com.castle.utils;

import com.castle.Main;
import com.castle.Testes;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

public class ReadFile {
    private static final Logger logger = LoggerFactory.getLogger(ReadFile.class);

    public File getFile(String fileName) {
        File file = null;
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            file = whenConvertingAnInProgressInputStreamToFile_thenCorrect2(inputStream);
        } catch (IOException e) {
            logger.error("Error ao ler arquivo.", e);
        }
        return file;
    }

    public File whenConvertingAnInProgressInputStreamToFile_thenCorrect2(InputStream inputStream)
            throws IOException {

        File targetFile = new File("C:/APP/hustle-castle-bot/targetFile.tmp");
      //  File targetFile = new File("/home/wellington/hustle-castle-bot/targetFile.tmp");

        java.nio.file.Files.copy(
                inputStream,
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        IOUtils.closeQuietly(inputStream);
        return targetFile;
    }
}
