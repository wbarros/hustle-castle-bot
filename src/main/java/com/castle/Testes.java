package com.castle;

import com.castle.image.processing.FindImage;
import com.castle.image.processing.ImageToText;
import com.castle.utils.ReadFile;
import org.apache.commons.io.IOUtils;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

public class Testes {
    private static final Logger logger = LoggerFactory.getLogger(Testes.class);
    private static String URL_TESTES = "testes/";
    private static String FORMATO_ARQUIVO = ".png";

    public static void teste1() {
        FindImage findImage = FindImage.getInstance();
        int poder = findImage.getPower();
        System.out.println("Poder: "+poder);

        Main.continuar = false;
    }





}