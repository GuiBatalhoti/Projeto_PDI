/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manipulacao;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author Guilherme
 */
public class FiltrosSegundoBim {
    
    private static double[][] DCT;

    public static double[][] getDCT() {
        return DCT;
    }

    public static void setDCT(double[][] DCT) {
        FiltrosSegundoBim.DCT = DCT;
    }
    
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
                    Color cor = new Color(0,0, tom*4);
                    saida.setRGB(i, j, cor.getRGB());
                }
                else if(tom >= 64 && tom < 128)
                {
                    Color cor = new Color(0, (tom-64) * 4, 255);
                    saida.setRGB(i, j, cor.getRGB());
                }
                else if (tom >= 128 && tom < 192)
                {
                    Color cor = new Color(0, 255, 255 - (tom - 128)*4);
                    saida.setRGB(i, j, cor.getRGB());
                }
                else 
                {
                    Color cor = new Color( (tom - 192)*4, 255, 0);
                    saida.setRGB(i, j, cor.getRGB());
                }
            }
        }
        
        return saida;
    }
    
    public static BufferedImage DCT(BufferedImage img)
    {
        int w = img.getWidth();
        int h = img.getHeight();
        
        DCT = new double[w][h];
        int preSaida[][] = new int[w][h];
        BufferedImage saida = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        double ci, cj, dct1;
  
        for (int u = 0; u < w; u++) {
            for (int v = 0; v < h; v++) {

                if (u == 0)
                    ci = Math.sqrt(1.0f/w);
                else
                    ci = Math.sqrt(2.0f/w);
                
                if (v == 0)
                    cj = Math.sqrt(1.0f/h);
                else
                    cj = Math.sqrt(2.0f/h);
                
                double sum = 0;
                
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        
                        int tom = (img.getRGB(x, y) & 0xff);
                        
                        dct1 = tom *
                              Math.cos( (2.0f * x + 1.0f) * u * Math.PI / (2.0f * w) ) *
                              Math.cos( (2.0f * y + 1.0f) * v * Math.PI / (2.0f * h) );
                        sum += dct1;
                    }
                }
                
                DCT[u][v] = (ci * cj * sum);
                preSaida[u][v] = (int) Math.round(DCT[u][v]);
            }
        }

        preSaida = FiltrosPrimeiroBim.normalizaImg(preSaida);
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                saida.setRGB(i, j, preSaida[i][j] | (preSaida[i][j] << 8) | (preSaida[i][j] << 16));
            }
        }
        
        return saida;
    }
    
    public static BufferedImage IDCT(double matriz[][])
    {
        BufferedImage saida = new BufferedImage(matriz.length, matriz[0].length, BufferedImage.TYPE_INT_RGB);

        double ci, cj, idct;
        
        for (int x = 0; x < matriz.length; x++) {
            for (int y = 0; y < matriz[x].length; y++) {
                double sum = 0;
                
                
                for (int u = 0; u < matriz.length; u++) {
                    for (int v = 0; v < matriz[u].length; v++) {
                        
                        if (u == 0)
                            ci = Math.sqrt(1.0f/matriz.length);
                        else
                            ci = Math.sqrt(2.0f/matriz.length);

                        if (v == 0)
                            cj = Math.sqrt(1.0f/matriz[u].length);
                        else
                            cj = Math.sqrt(2.0f/matriz[u].length);
                        
                        double tom = matriz[u][v];
                        
                        idct = (tom *
                                Math.cos((2.0f * x + 1.0f) * u * Math.PI / (2.0f * matriz.length)) *
                                Math.cos((2.0f * y + 1.0f) * v * Math.PI / (2.0f * matriz[u].length)));
                        
                        sum += ci * cj * idct;
                    }
                }
                                
                int aux = (int) sum;
                saida.setRGB(x, y, aux  | aux << 8 | aux << 16);
            }
        }
        
        return saida;
    }
    
    public static double[][] passaAltaDCT(double matriz[][], int raio)
    {
        double saida[][] = new double[matriz.length][matriz[0].length];
        
        for (int i = 0; i < matriz.length; i++)
        {
            for (int j = 0; j < matriz[i].length; j++)
            {
                if (Math.round(Math.sqrt(i*i + j*j)) >= raio)
                    saida[i][j] = matriz[i][j];
                else
                    saida[i][j] = 0.0;
            }
        }
        
        return saida;
    }
        
    public static double[][] passaBaixaDCT(double matriz[][], int raio)
    {
        double saida[][] = new double[matriz.length][matriz[0].length];
        
        for (int i = 0; i < matriz.length; i++)
        {
            for (int j = 0; j < matriz[i].length; j++)
            {
                if (Math.round(Math.sqrt(i*i + j*j)) >= raio)
                    saida[i][j] = 0.0;
                else
                    saida[i][j] = matriz[i][j];
            }
        }
        return saida;
    }
    
    public static BufferedImage equalizacaoHSI(BufferedImage img)
    {
        //contagem do número de pixels de cada 
        int contagem[] = contagemIntensidade(img);

        //valor aux constante para o cálculo de equalização
        int aux = img.getWidth() * img.getHeight();

        //imagem de saída equalizada
        BufferedImage imgRealce = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                int freqAcumulada = 0; //frequencia acumulada

                int rgb = img.getRGB(i, j); //pegando o valor RGB do pixel

                //manipulação dos bit para pegar o valor de uma cor específica
                int red = 0xff & (rgb >> 16);
                int green = 0xff & (rgb >> 8);
                int blue = 0xff & rgb;
                
                int[] hsl = Conversor.rgbToHSL(red, green, blue);
                 
                //contagem da frequencia acumulada
                for (int k = 0; k <= hsl[2]-1; k++)
                    freqAcumulada += contagem[k];

                //cálculo do pixel equalizado
                int valorEq = Math.max(0, Math.round(((240 * freqAcumulada) / aux)) - 1);
                  
                int[] cor = Conversor.hslToRGB(hsl[0], hsl[1], valorEq);
                
                //colocando o valor dentor da imagem de saída
                imgRealce.setRGB(i, j, cor[2] | (cor[1] << 8) | (cor[0] << 16));
            }
        }
        
        return imgRealce;
    }
    
    public static int[] contagemIntensidade(BufferedImage img)
    {
        int[] contagem = new int[240];
        for (int i = 0; i < 240; i++) {
            contagem[i] = 0; //preenchimento do vetor de contagem
        }
        
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                
                int rgb = img.getRGB(i, j); //pegando o valor RGB do pixel

                //manipulação dos bit para pegar o valor de uma cor específica
                int red = 0xff & (rgb >> 16);
                int green = 0xff & (rgb >> 8);
                int blue = 0xff & rgb;
                
                int[] hsl = Conversor.rgbToHSL(red, green, blue);
                
                if (hsl[2] == 240)
                    contagem[239]++;
                else  
                    contagem[hsl[2]-1]++;
            }
        }
        
        return contagem;
    }
}
