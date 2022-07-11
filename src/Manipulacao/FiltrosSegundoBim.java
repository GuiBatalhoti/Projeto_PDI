/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manipulacao;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author Guilherme
 */
public class FiltrosSegundoBim {
    
    public static BufferedImage filtroMinimo(BufferedImage img)
    {
        // imagem de saida
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //percorrendo a imagem
        for (int i = 1; i < img.getWidth()-1; i++)
        {
            for (int j = 1; j < img.getHeight()-1; j++)
            {
                //região
                int regiao[] = new int[9];
                
                //percorrendo a região
                for (int k = -1; k < 2; k++)
                {
                    for (int l = -1; l < 2; l++)
                    {
                        regiao[(k+1)*3 + l+1] = img.getRGB(i+k, j+l) & 0xff;
                    }
                }
                
                //menor valor da região
                int min = Arrays.stream(regiao).min().getAsInt();
                
                //colcoando na imagem de saída
                saida.setRGB(i, j, min | (min << 8) | (min << 16));
            }
        }
        
        //retorno
        return saida;
    }
    
    public static BufferedImage filtroMaximo(BufferedImage img)
    {
        //imagem de saída
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //percorrendo a imagem
        for (int i = 1; i < img.getWidth()-1; i++)
        {
            for (int j = 1; j < img.getHeight()-1; j++)
            {
                //região
                int regiao[] = new int[9];
                
                //percorrendo a região
                for (int k = -1; k < 2; k++)
                {
                    for (int l = -1; l < 2; l++)
                    {
                        regiao[(k+1)*3 + l+1] = img.getRGB(i+k, j+l) & 0xff;   
                    }
                }
                
                //maior valor da região
                int max = Arrays.stream(regiao).max().getAsInt();
                
                //colcando na saída
                saida.setRGB(i, j, max | (max << 8) | (max << 16));
            }
        }
        
        //retorno
        return saida;
    }
    
    public static BufferedImage filtroPontoMedio(BufferedImage img)
    {
        //imagem de saída
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //percorrendo a imagem
        for (int i = 1; i < img.getWidth()-1; i++)
        {
            for (int j = 1; j < img.getHeight()-1; j++)
            {
                //região
                int regiao[] = new int[9];
                
                //percorrendo a região
                for (int k = -1; k < 2; k++)
                {
                    for (int l = -1; l < 2; l++)
                    {
                        regiao[(k+1)*3 + l+1] = img.getRGB(i+k, j+l) & 0xff;   
                    }
                }
                
                //menor e maior valor da região
                int min = Arrays.stream(regiao).min().getAsInt();
                int max = Arrays.stream(regiao).max().getAsInt();
                
                int media = (max + min) / 2;
                
                //colcando na saída
                saida.setRGB(i, j, media | (media << 8) | (media << 16));
            }
        }
        
        //retorno
        return saida;
    }
}
