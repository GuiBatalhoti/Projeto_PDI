/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulacao;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Guilherme
 */
public class AberturaImg {
    
    private static AberturaImg instance;
    private static BufferedImage img;
    private static BufferedImage imgCinza;
    private static BufferedImage imgLimiar;
    
    public static AberturaImg getInstance() {
        if (instance == null)
            return new AberturaImg();
        return instance;
    }

    public AberturaImg() {
    }
    
    public void abreImg(File arquivo)
    {
        try
        {
            this.img = ImageIO.read(arquivo);
            //System.out.println("Tamanho da imagem: " + this.img.getHeight()+ " x " + this.img.getWidth());
        }
        catch(Exception e){}
    }
    
    public void salvarImg(BufferedImage img)
    {
        try
        {
            File arquivo = new File("imagem.png");
            ImageIO.write(img, "png", arquivo);
        }
        catch(Exception e){}
    }
    
    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public static BufferedImage getImgLimiar() {
        return imgLimiar;
    }

    public BufferedImage getImgCinza() {
        return imgCinza;
    }

    public void setImgCinza(BufferedImage imgCinza) {
        this.imgCinza = imgCinza;
    }

    public void setImgLimiar(BufferedImage imgLimiar) {
        this.imgLimiar = imgLimiar;
    }
    
}
