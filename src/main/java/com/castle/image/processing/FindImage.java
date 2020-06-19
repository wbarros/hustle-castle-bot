package com.castle.image.processing;

import com.castle.config.HustleCastleBotConfig;
import com.castle.utils.ReadFile;
import com.castle.utils.Utils;
import org.sikuli.api.*;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FindImage {
    private static final Logger logger = LoggerFactory.getLogger(FindImage.class);
    private static FindImage instance;
    private final int TELA_X = 0;
    private final int TELA_Y = 32;
    private final int TELA_WIDTH = 1020;
    private final int TELA_HEIGHT = 578;
    private static HustleCastleBotConfig config;

    private FindImage() {};

    public static synchronized FindImage getInstance() {
        if(instance == null) {
            instance = new FindImage();
        }
        return instance;
    }

    public int getPower(int x, int y, int width, int height) {
        config =  HustleCastleBotConfig.getInstance();
        ScreenRegion s = new DesktopScreenRegion(0, TELA_X, TELA_Y, TELA_WIDTH, TELA_HEIGHT);
        Mouse mouse = new DesktopMouse();

        /*Target imageTargetI = new ImageTarget(new ReadFile().getFile(Constantes.ARENA_I_PONTUACAO));
        Target imageTargetMachado = new ImageTarget(new ReadFile().getFile(Constantes.ARENA_MACHADO_PONTUACAO));
        ScreenRegion r = s.wait(imageTargetI, 1000);

        if(r != null) {
            //mouse.move(r.getCenter());
        } else {
            return Integer.MAX_VALUE;
        }
        ScreenRegion r2 = s.wait(imageTargetMachado, 1000);

        if(r2 != null) {
            //mouse.move(r2.getCenter());
        } else {
            return Integer.MAX_VALUE;
        } */

        /*Rectangle rectangle = new Rectangle(r2.getUpperRightCorner().getX(), r2.getUpperRightCorner().getY(),0 , 0);
        rectangle.add(r.getUpperLeftCorner().getX(), r.getUpperLeftCorner().getY());
        rectangle.add(r.getLowerLeftCorner().getX(), r.getLowerLeftCorner().getY());
        rectangle.add(r2.getLowerRightCorner().getX(), r2.getLowerRightCorner().getY()); */

/*        System.out.println("x1=" +r2.getUpperRightCorner().getX() + " y1="+r2.getUpperRightCorner().getY());
        System.out.println("x2=" +r.getUpperLeftCorner().getX() + " y2="+r.getUpperLeftCorner().getY());
        System.out.println("x4=" +r.getLowerLeftCorner().getX() + " y3="+r.getLowerLeftCorner().getY());
        System.out.println("x4=" +r2.getLowerRightCorner().getX() + " y3="+r2.getLowerRightCorner().getY());*/

        ScreenRegion result = new DesktopScreenRegion(0, x, y, width, height);
        BufferedImage bf = result.capture();
        BufferedImage bfEdited = Utils.converterPixelBlacktoWhite(bf, 205); // todos os pixels que for menor que 205 no RGB 0 a 255, será 0

        ImageToText imageToText = new ImageToText();
        String power = imageToText.converterFromBuffered(bfEdited);

        return Utils.stringToInt(power);

    }

    public ScreenRegion findImage(String imageName) {
        ScreenRegion r;
        ScreenRegion s = new DesktopScreenRegion(0, TELA_X, TELA_Y, TELA_WIDTH, TELA_HEIGHT);
        Target imageTargetI = new ImageTarget(new ReadFile().getFile(imageName));
        r = s.wait(imageTargetI, 1000);
        return r;
    }

    public boolean imageExists(String imageName) {
        ScreenRegion r = null;
        ScreenRegion s = new DesktopScreenRegion(0, TELA_X, TELA_Y, TELA_WIDTH, TELA_HEIGHT);
        try {
            Target imageTargetI = new ImageTarget(new ReadFile().getFile(imageName));
            r = s.wait(imageTargetI,1000);
        } catch (Exception e) {
            logger.error("Erro ao verificar imagem: {}",imageName, e);
        }
        if(r == null) {
            return false;
        } else if (r.getScore() >= 0.9) {
            return true;
        }
        return false;
    }

}
