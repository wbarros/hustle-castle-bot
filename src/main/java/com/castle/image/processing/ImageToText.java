package com.castle.image.processing;

import com.castle.config.HustleCastleBotConfig;
import com.castle.utils.ReadFile;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageToText {
    private static final Logger logger = LoggerFactory.getLogger(ImageToText.class);
    private static Tesseract tesseract;
    private static String DATA_PATH_LINUX = "/usr/share/tesseract-ocr/4.00/tessdata/";
    private static String DATA_PATH_WINDOWS = "C:/Users/Wellington/AppData/Local/Tesseract-OCR/tessdata/";
    private static HustleCastleBotConfig config;

    public String converterFromFile(String fileName) {
        String text = null;
        File file = new ReadFile().getFile(fileName);
        tesseract = getTesseract();
        if (file != null) {
            try {
                text = tesseract.doOCR(file);
            } catch (TesseractException e) {
               logger.error("Erro ao processar OCR", e);
            }
        }
        return text.replaceAll("\\D", "");
    }

    public String converterFromClipboard(File clip) {
        String text = null;
        tesseract = getTesseract();

        return text.replaceAll("\\D", "");
    }

    public String converterFromBuffered(BufferedImage bufferedImage) {
        tesseract = getTesseract();
        config =  HustleCastleBotConfig.getInstance();
        String text = null;
        try {
            if(config.isSavePowers()) {
                File outputfile = new File("C:/APP/hustle-castle-bot/"+System.nanoTime()+".png");
                ImageIO.write(bufferedImage, "png", outputfile);
            }
        } catch (IOException e) {
            logger.error("Erro ao salvar imagem do poder do adversario", e);
        }

        try {
            text = tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            logger.error("Erro ao processar OCR", e);
        }

        return text.replaceAll("O","0").replaceAll("\\D", "");
    }

    private static Tesseract getTesseract() {
        if (tesseract == null) {
            tesseract = new Tesseract();
            tesseract.setDatapath(DATA_PATH_WINDOWS);
            tesseract.setPageSegMode(13);
            tesseract.setLanguage("eng");
            tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");
            tesseract.setTessVariable("tessedit_pageseg_mode", "13");
            tesseract.setTessVariable("load_system_dawg", "0");
            tesseract.setTessVariable("load_freq_dawg", "0");
        }
        return tesseract;
    }
}
