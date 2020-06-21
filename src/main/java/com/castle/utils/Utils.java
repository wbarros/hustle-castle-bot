package com.castle.utils;

import com.castle.Main;
import com.castle.image.processing.FindImage;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Utils {

    public static BufferedImage converterPixelBlacktoWhite(BufferedImage image, int limiar) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        result.getGraphics().drawImage(image, 0, 0, null);
        WritableRaster raster = result.getRaster();
        int[] pixels = new int[image.getWidth()];
        for (int y = 0; y < image.getHeight(); y++) {
            raster.getPixels(0, y, image.getWidth(), 1, pixels);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] < limiar)
                    pixels[i] = 0;
                else
                    pixels[i] = 255;
            }
            raster.setPixels(0, y, image.getWidth(), 1, pixels);
        }
        return result;
    }

    public static int stringToInt(String power) { //caso o power venha null ou vazio vai ser considerado um power muito alto e nao ira batalhar com esse cara.
        if(power != null && !power.equals(""))
            return Integer.parseInt(power);
        else
            return Integer.MAX_VALUE;
    }

    public static void esperarEventoAcontecerEClicar(String nomeImagem, String msg) {
        try {
            FindImage findImage = FindImage.getInstance();
            while(!findImage.imageExists(nomeImagem) && Main.continuar) {
                System.out.println(msg);
                Thread.sleep(2000);
                Utils.sairDoModoOcioso();
            }
            encontraImagemParaClicar(nomeImagem);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	public static void esperarEventoAcontecerCliqueNaoOcioso(String nomeImagem, String msg) {
        try {
            FindImage findImage = FindImage.getInstance();
            int i = 0;
            while(findImage.imageExists(nomeImagem) && Main.continuar) {
                System.out.println(msg);
                if (i == 15) {
                    encontraImagemParaClicar("arena/botao_arena_cancelar.png");
                } else if( i == 16) {
                    encontraImagemParaClicar("arena/botao_arena_sem_comida.png");
                    i=0;
                }
                Thread.sleep(2000);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void esperarEventoTerminar(String nomeImagem, String msg) {
        try {
            FindImage findImage = FindImage.getInstance();
            while(findImage.imageExists(nomeImagem) && Main.continuar) {
                System.out.println(msg);
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sairDoModoOcioso() throws InterruptedException {
        if(encontraImagemParaClicar("arena/botao_atualizar_ocioso.png")) {
            System.out.println("Ops entrei no modo ocioso,\nvamos sair dessa...");
            Thread.sleep(10000);
            if(encontraImagemParaClicar("arena/botao_mapa_tela_inicial.png"))
                System.out.println("Encontrei o mapa");
            else
                System.out.println("Não encontrei o mapa");
            Thread.sleep(10000);
            if(encontraImagemParaClicar("arena/imagem_arena_entrar.png"))
                System.out.println("Encontrei a Arena");
            else
                System.out.println("Não encontrei a Arena");
        }
        if(Utils.encontraImagemParaClicar("arena/botao_arena_resgatar_recompensa.png")) {
            System.out.println("Resgatei a recompensa");
        } else if(Utils.encontraImagemParaClicar("arena/botao_arena_fim.png")) {
            System.out.println("Fim da arena");
        }
    }

    public static boolean encontraImagemParaClicar(String imagem) {
        FindImage findImage = FindImage.getInstance();
        Mouse mouse = new DesktopMouse();
        ScreenRegion screenRegion = findImage.findImage(imagem);
        if(screenRegion != null) {
            mouse.move(screenRegion.getCenter());
            mouse.click(screenRegion.getCenter());
            return true;
        }
        return false;
    }


}
