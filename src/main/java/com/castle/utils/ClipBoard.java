package com.castle.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ClipBoard {
    // tratar erros e retornar null em todos metodos
    public File getClipboardToFile() throws IOException, UnsupportedFlavorException {
        File outputfile = new File("saved.png");
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        BufferedImage clip = (BufferedImage) cb.getData(DataFlavor.imageFlavor);
        ImageIO.write(clip, "png", outputfile);
        return outputfile;
    }

    public BufferedImage getClipboardToBufferedImage() throws IOException, UnsupportedFlavorException {
        File outputfile = new File("saved.png");
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        BufferedImage clip = (BufferedImage) cb.getData(DataFlavor.imageFlavor);
        return clip;
    }
}
