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
    
    private static double[][] matrizDCT;
    
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
    
    public static BufferedImage colorizacao(BufferedImage img)
    {
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < img.getWidth(); i++)
        {
            for (int j = 0; j < img.getHeight(); j++)
            {
                int tom = img.getRGB(i, j) & 0xff;
                
                if (tom < 64)
                {
                    saida.setRGB(i, j, (tom*4));
                }
                else if(tom < 128)
                {
                    saida.setRGB(i, j, 255 | ((tom-64) * 4) << 8);
                }
                else if (tom < 192)
                {
                    saida.setRGB(i, j, 255 - (tom - 128)*4 | (255 << 8));
                }
                else 
                {
                    saida.setRGB(i, j, (255 << 8) | ((tom - 192)*4) << 16 );
                }
            }
        }
        
        return saida;
    }
    
    public static BufferedImage DCT(BufferedImage img)
    {
        matrizDCT = new double[img.getWidth()][img.getHeight()];
        int preSaida[][] = new int[img.getWidth()][img.getHeight()];
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        double ci, cj, dct, sum;

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {

                // ci and cj depends on frequency as well as
                // number of row and columns of specified matrix
                if (i == 0)
                    ci = (double) (1 / Math.sqrt(img.getWidth()));
                else
                    ci = (double) (Math.sqrt(2) / Math.sqrt(img.getWidth()));
                if (j == 0)
                    cj = (double) (1 / Math.sqrt(img.getHeight()));
                else
                    cj = (double) (Math.sqrt(2) / Math.sqrt(img.getHeight()));

                // sum will temporarily store the sum of
                // cosine signals
                sum = 0;
                for (int k = 0; k < img.getWidth(); k++) {
                    for (int l = 0; l < img.getHeight(); l++) {
                        dct = (double) ((img.getRGB(k, l) & 0xff) *
                                Math.cos((2 * k + 1) * i * Math.PI / (2 * img.getWidth())) *
                                Math.cos((2 * l + 1) * j * Math.PI / (2 * img.getHeight())));
                        sum += dct;
                    }
                }
                
                matrizDCT[i][j] = (ci * cj * sum);
                preSaida[i][j] = ((int) (ci * cj * sum));
            }
        }
        
        preSaida = FiltrosPrimeiroBim.normalizaImg(preSaida);
        
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                saida.setRGB(i, j, preSaida[i][j] | (preSaida[i][j] << 8) | (preSaida[i][j] << 16));
            }
        }
        
        return saida;
    }
    
    public static BufferedImage IDCT()
    {
        int preSaida[][] = new int[matrizDCT.length][matrizDCT[0].length];
        BufferedImage saida = new BufferedImage(matrizDCT.length, matrizDCT[0].length, BufferedImage.TYPE_INT_RGB);

        double ci, cj, idct, sum;

        for (int i = 0; i < matrizDCT.length; i++) {
            for (int j = 0; j <  matrizDCT[0].length; j++) {

                // ci and cj depends on frequency as well as
                // number of row and columns of specified matrix
                if (i == 0)
                    ci = (double) (1 / Math.sqrt( matrizDCT.length));
                else
                    ci = (double) (Math.sqrt(2) / Math.sqrt(matrizDCT.length));
                if (j == 0)
                    cj = (double) (1 / Math.sqrt(matrizDCT[0].length));
                else
                    cj = (double) (Math.sqrt(2) / Math.sqrt(matrizDCT[0].length));

                // sum will temporarily store the sum of
                // cosine signals
                sum = 0;
                for (int k = 0; k < matrizDCT.length; k++) {
                    for (int l = 0; l < matrizDCT[0].length; l++) {
                        idct = (double) (ci * cj * matrizDCT[k][l] *
                                Math.cos((2 * k + 1) * i * Math.PI / (2 * matrizDCT.length)) *
                                Math.cos((2 * l + 1) * j * Math.PI / (2 * matrizDCT[0].length)));
                        sum += idct;
                    }
                }
                
                int aux = (int) Math.ceil(sum);

                preSaida[i][j] = aux;
            }
        }
        
        preSaida = FiltrosPrimeiroBim.normalizaImg(preSaida);
        
        for (int i = 0; i < matrizDCT.length; i++) {
            for (int j = 0; j <  matrizDCT[0].length; j++) {
                saida.setRGB(i, j, preSaida[i][j] | (preSaida[i][j] << 8) | (preSaida[i][j] << 16));
            }
        }
        
        return saida;
    }
}
