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
    private static BufferedImage imgDCT;
    
    public static double[][] getDCT() {
        return DCT;
    }

    public static void setDCT(double[][] DCT) {
        FiltrosSegundoBim.DCT = DCT;
    }

    public static BufferedImage getImgDCT() {
        return imgDCT;
    }

    public static void setImgDCT(BufferedImage imgDCT) {
        FiltrosSegundoBim.imgDCT = imgDCT;
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
        
        imgDCT = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        DCT = new double[w][h];
        int preSaida[][] = new int[w][h];
        
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
                imgDCT.setRGB(i, j, preSaida[i][j] | (preSaida[i][j] << 8) | (preSaida[i][j] << 16));
            }
        }
        
        return imgDCT;
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
                                
                int aux = (int) Math.abs(Math.round(sum));
                if (aux > 255)
                    aux = 255;
                Color cor = new Color(aux, aux, aux);
                saida.setRGB(x, y, cor.getRGB());
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
                if (Math.round(Math.sqrt(i*i + j*j)) <= raio)
                    saida[i][j] = 0.0;
                else
                    saida[i][j] = matriz[i][j];
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
                if (Math.round(Math.sqrt(i*i + j*j)) <= raio)
                    saida[i][j] = matriz[i][j];
                else
                    saida[i][j] = 0.0;

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
                    contagem[hsl[2]]++;
            }
        }
        
        return contagem;
    }
    
    public static BufferedImage laplacianoGaussiana(BufferedImage img)
    {
        float[][] mascara = {
                           {0, 0, -1, 0, 0},
                           {0, -1, -2, -1, 0},
                           {-1, -2, 16, -2, -1},
                           {0, -1, -2, -1, 0},
                           {0, 0, -1, 0, 0}
                          };
                

        BufferedImage imgSaida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 2; i < imgSaida.getWidth() - 2; i++) 
        {
            for (int j = 2; j < imgSaida.getHeight() - 2; j++) 
            {

                int soma = 0;

                for (int k = -2; k < mascara.length - 2; k++) 
                {
                    for (int l = -2; l < mascara[k+2].length - 2; l++) 
                    {
                        soma += (int) (img.getRGB(i + k, j + l) & 255) * mascara[k + 2][l + 2];
                    }
                }
                

                soma = Math.abs(soma/16);

                imgSaida.setRGB(i, j, soma | (soma << 8) | (soma << 16));
            }
        }
        
        imgSaida = FiltrosPrimeiroBim.normalizaImg(imgSaida);

        return imgSaida;
    }
    
    public static int otsuThreshold(BufferedImage img)
    {
        float[] contagem = new float[256];
        Arrays.fill(contagem, 0);
        float tamanhoImg = img.getWidth() * img.getHeight();
        int candidato = 0;
        
        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                int tom = img.getRGB(i, j) & 255;
                contagem[tom]++;
            }
        }
        
        float mediaGlobal = 0;
        for (int i = 0; i < 256; i++)
        {
            mediaGlobal += contagem[i] * i;
        }
                
        double varianca;
        double melhorVarianca = Double.NEGATIVE_INFINITY;

        double mediaFundo = 0;
        double pesoFundo = 0;

        double mediaFrente = (double) mediaGlobal / (double) tamanhoImg;
        double pesoFrente = tamanhoImg;
        double diferencacMedias;

        int t = 0;
        while (t < 255) {
            diferencacMedias = mediaFrente - mediaFundo;
            varianca = pesoFundo * pesoFrente * diferencacMedias * diferencacMedias;

            if (varianca > melhorVarianca) {
                melhorVarianca = varianca;
                candidato = t;
            }

            while (t < 255 && contagem[t] == 0)
                t++;

            mediaFundo = (mediaFundo * pesoFundo + contagem[t] * t) / (pesoFundo + contagem[t]);
            mediaFrente = (mediaFrente * pesoFrente - contagem[t] * t) / (pesoFrente - contagem[t]);
            pesoFundo += contagem[t];
            pesoFrente -= contagem[t];
            t++;
        }
        
        return candidato;
   }
    
    public static BufferedImage dilatacao(BufferedImage img){
        
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_INT_RGB);
        int  i, j, m, n;
        int mascara[][] = {{0,1,0},{1,1,1},{0,1,0}};
        Color cor, preto = new Color(0,0,0);
        
        for(i=0; i<saida.getWidth()-1; i++)
            for(j=0; j<saida.getHeight()-1; j++)
                saida.setRGB(i, j, 0);
        
        
        for(i=1; i<img.getWidth()-1; i++)
            for(j=1; j<img.getHeight()-1; j++){
                cor = new Color(img.getRGB(i, j));
                if(!cor.equals(preto))
                    for(m=-1; m<=1; m++)
                        for(n=-1; n<=1; n++)
                            if(mascara[m+1][n+1] == 1)
                                saida.setRGB(i+m, j+n, cor.getRGB());
            }
        
        return saida;
    }
    
    public static BufferedImage erosao(BufferedImage img){
        
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_INT_RGB);
        int i, j, m, n;
        int mascara[][] = {{0,1,0},{1,1,1},{0,1,0}};
        Boolean remove;
        Color cor, preto = new Color(0,0,0);
        
        for(i=0; i<saida.getWidth(); i++)
            for(j=0; j<saida.getHeight(); j++)
                saida.setRGB(i, j, 0);
        
        for(i=1; i<img.getWidth()-1; i++)
            for(j=1; j<img.getHeight(); j++){
                cor = new Color(img.getRGB(i, j));
                if(!cor.equals(preto)){
                    remove = false;
                    for(m=-1; m<=1; m++)
                        for(n=-1; n<=1; n++)
                            if(mascara[m+1][n+1] == 1 && img.getRGB(i+m, j+n) == preto.getRGB())
                                remove = true;
                    if(remove)
                        saida.setRGB(i, j, preto.getRGB());
                    else
                        saida.setRGB(i, j, cor.getRGB());
                }
            }
        
        return saida;
    }
}
