/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulacao;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author Guilherme
 */
public class MascarasPrimeiroBim 
{

    public static BufferedImage convolucaoGenerica(BufferedImage img, float[][] mascara) 
    {
        if (img == null) {
            return null;
        }
        //montando a imagem de saída
        BufferedImage imgSaida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        //percorrendo a imagemd e saída
        for (int i = 1; i < imgSaida.getWidth() - 1; i++) 
        {
            for (int j = 1; j < imgSaida.getHeight() - 1; j++) 
            {
                //armazena o tom do pixel
                float tom = 0;
                //percorre a máscara e faz o cálculo com a máscara
                for (int k = -1; k < mascara.length - 1; k++) 
                {
                    for (int l = -1; l < mascara[0].length - 1; l++) 
                    {
                        tom += (img.getRGB(i + k, j + l) & 255) * mascara[k + 1][l + 1];
                    }
                }
                /*
                seria a mesma coisa q o loop acima:
                tons += (abertura.getImgCinza().getRGB(i-1, j-1) & 0xff);;
                tons += (abertura.getImgCinza().getRGB(i-1, j) & 0xff);
                tons += (abertura.getImgCinza().getRGB(i-1, j+1) & 0xff);
                tons += (abertura.getImgCinza().getRGB(i, j-1) & 0xff);
                tons += (abertura.getImgCinza().getRGB(i, j) & 0xff);
                tons += (abertura.getImgCinza().getRGB(i, j+1) & 0xff);
                tons += (abertura.getImgCinza().getRGB(i+1, j-1) & 0xff);
                tons += (abertura.getImgCinza().getRGB(i+1, j) & 0xff);
                tons += (abertura.getImgCinza().getRGB(i+1, j+1) & 0xff);
                 */
                //trocando o valor do tom de float para int
                int tom_int = (int) tom;
                //colocando na imagem de saída
                imgSaida.setRGB(i, j, tom_int | (tom_int << 8) | (tom_int << 16));
            }
        }
        return imgSaida;
    }

    public static BufferedImage mascaraMediana(BufferedImage img) 
    {
        
        if (img == null) 
        {
            return null;
        }
        
        //motagem da imgaem de saída vazia
        BufferedImage imgSaida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //percorrendo a imagem principal
        for (int i = 1; i <= img.getWidth() - 2; i++) 
        {
            for (int j = 1; j <= img.getHeight() - 2; j++) 
            {
                int[] tons = new int[9]; //vetor de tons
                
                //pegando os tons da vizinhança 8 de um pixel
                tons[0] = (img.getRGB(i - 1, j - 1) & 255);
                tons[1] = (img.getRGB(i - 1, j) & 255);
                tons[2] = (img.getRGB(i - 1, j + 1) & 255);
                tons[3] = (img.getRGB(i, j - 1) & 255);
                tons[4] = (img.getRGB(i, j) & 255);
                tons[5] = (img.getRGB(i, j + 1) & 255);
                tons[6] = (img.getRGB(i + 1, j - 1) & 255);
                tons[7] = (img.getRGB(i + 1, j) & 255);
                tons[8] = (img.getRGB(i + 1, j + 1) & 255);
                
                //organizando os tons de forma crescente
                Arrays.sort(tons);
                
                //colocando na imagem de saída
                imgSaida.setRGB(i, j, tons[4] | (tons[4] << 8) | (tons[4] << 16));
            }
        }
        return imgSaida;
    }
    
    public static BufferedImage filtroSobelVertical(BufferedImage img)
    {
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        float[][] filtroVertical = {{-1.0F, -2.0F, -1.0F}, 
                                    {0.0F, 0.0F, 0.0F}, 
                                    {1.0F, 2.0F, 1.0F}};
        
        int maxGradient = -1;
        
        int[][] bordas = new int[img.getWidth()][img.getHeight()];
        
        for (int i = 1; i < img.getWidth() - 1; i++) 
        {
            for (int j = 1; j < img.getHeight() - 1; j++) 
            {
                int auxV = 0;
                for (int k = -1; k < 2; k++) 
                {
                    for (int l = -1; l < 2; l++) 
                    {
                        auxV += (int) ((img.getRGB(i + k, j + l) & 255) * filtroVertical[k + 1][l + 1]);
                    }
                }
                
                auxV = (int) Math.sqrt(auxV*auxV);
                
                if (maxGradient < auxV) {
                    maxGradient = auxV;
                }
                bordas[i][j] = auxV;
            }
        }
        
        double escala = 255.0F / maxGradient;
        
        for (int i = 1; i < img.getWidth() - 1; i++) 
        {
            for (int j = 1; j < img.getHeight() - 1; j++)
            {
                int tom = (int) (bordas[i][j] * escala);
                saida.setRGB(i, j, tom | (tom << 8) | (tom << 16));
            }
        }
        
        return saida;   
    }
    
    public static BufferedImage filtroSobelHorizontal(BufferedImage img)
    {
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        float[][] filtroHorizontal = {{-1.0F, 0.0F, 1.0F},
                                      {-2.0F, 0.0F, 2.0F},
                                      {-1.0F, 0, 1.0F}};
        
        int maxGradient = -1;
        
        int[][] bordas = new int[img.getWidth()][img.getHeight()];
        
        for (int i = 1; i < img.getWidth() - 1; i++) 
        {
            for (int j = 1; j < img.getHeight() - 1; j++) 
            {
                int auxH = 0;
                for (int k = -1; k < 2; k++) 
                {
                    for (int l = -1; l < 2; l++) 
                    {
                        auxH += (int) ((img.getRGB(i + k, j + l) & 255) * filtroHorizontal[k + 1][l + 1]);
                    }
                }
                
                auxH = (int) Math.sqrt(auxH*auxH);
                
                if (maxGradient < auxH) {
                    maxGradient = auxH;
                }
                bordas[i][j] = auxH;
            }
        }
        
        double escala = 255.0F / maxGradient;
        
        for (int i = 1; i < img.getWidth() - 1; i++) 
        {
            for (int j = 1; j < img.getHeight() - 1; j++)
            {
                int tom = (int) (bordas[i][j] * escala);
                saida.setRGB(i, j, tom | (tom << 8) | (tom << 16));
            }
        }
        
        return saida; 
    }

    public static BufferedImage bordasFiltroSobel(BufferedImage img) 
    {
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        float[][] filtroVertical = {{-1.0F, -2.0F, -1.0F}, 
                                    {0.0F, 0.0F, 0.0F}, 
                                    {1.0F, 2.0F, 1.0F}};
        
        float[][] filtroHorizontal = {{-1.0F, 0.0F, 1.0F},
                                      {-2.0F, 0.0F, 2.0F},
                                      {-1.0F, 0, 1.0F}};
        
        int maxGradient = -1;
        
        int[][] bordas = new int[img.getWidth()][img.getHeight()];
        
        for (int i = 1; i < img.getWidth() - 1; i++) 
        {
            for (int j = 1; j < img.getHeight() - 1; j++) 
            {
                int auxV = 0;
                int auxH = 0;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        auxV += (int) ((img.getRGB(i + k, j + l) & 255) * filtroVertical[k + 1][l + 1]);
                        auxH += (int) ((img.getRGB(i + k, j + l) & 255) * filtroHorizontal[k + 1][l + 1]);
                    }
                }
                int raiz = (int) Math.sqrt(auxV * auxV + auxH * auxH);
                if (maxGradient < raiz) {
                    maxGradient = raiz;
                }
                bordas[i][j] = raiz;
            }
        }
        double escala = 255.0F / maxGradient;
        for (int i = 1; i < img.getWidth() - 1; i++) 
        {
            for (int j = 1; j < img.getHeight() - 1; j++)
            {
                int tom = (int) (bordas[i][j] * escala);
                saida.setRGB(i, j, tom | (tom << 8) | (tom << 16));
            }
        }
        return saida;
    }
    
    public static BufferedImage mascaraLaplace(BufferedImage img)
    {        
        float[][] mascaraLaplace = {{0f, -1f, 0f},
                                    {-1f, 4f, -1f},
                                    {0f, -1f, 0f}};
        
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int i = 1; i < img.getWidth()-1; i++)
        {
            for (int j = 1; j < img.getHeight()-1; j++)
            {
                int soma = 0;
                
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        soma += (int) ((img.getRGB(i + k, j + l) & 0xff) * mascaraLaplace[k + 1][l + 1]);
                    }
                }
                
                soma = Math.abs(soma/4);
                
                saida.setRGB(i, j, soma | (soma << 8) | (soma << 16));
            }
        }
        
        saida = FiltrosPrimeiroBim.normalizaImg(saida);
        
        return saida;
    }
}
