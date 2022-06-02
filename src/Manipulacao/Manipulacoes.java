/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulacao;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Guilherme
 */
public class Manipulacoes 
{

    public static BufferedImage tonsCinza(BufferedImage img) 
    {
        if (img == null) //se nada estiver aberto
        {
            return null;
        }

        //imagem de saída em tons de cinza
        BufferedImage cinza = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //loop duplo pela imagem
        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                int rgb = img.getRGB(i, j); //pegando o valor RGB do pixel

                //manipulação dos bit para pegar o valor de uma cor específica
                int blue = 0x0000ff & rgb;
                int green = 0x0000ff & (rgb >> 8);
                int red = 0x0000ff & (rgb >> 16);

                //cálculo do valor em tons de cinza
                int lum = (int) (red * 0.299 + green * 0.587 + blue * 0.114);

                //montagem da imagem de saída em tons de cinza
                cinza.setRGB(i, j, lum | (lum << 8) | (lum << 16));
            }
        }
        return cinza;
    }

    public static ArrayList<BufferedImage> canais()
    {
        AberturaImg abertura = AberturaImg.getInstance();

        if (abertura.getImg() == null) //se nada estiver aberto
        {
            return null;
        }

        //montagem das imagens de saída dos canais vermelho, verde e azul
        BufferedImage vermelho = new BufferedImage(abertura.getImg().getWidth(), abertura.getImg().getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage verde = new BufferedImage(abertura.getImg().getWidth(), abertura.getImg().getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage azul = new BufferedImage(abertura.getImg().getWidth(), abertura.getImg().getHeight(), BufferedImage.TYPE_INT_RGB);

        //loop duplo percorrendo a imagem
        for (int i = 0; i < abertura.getImg().getWidth(); i++) 
        {
            for (int j = 0; j < abertura.getImg().getHeight(); j++)
            {
                int rgb = abertura.getImg().getRGB(i, j); //pegando o valor RGB do pixel

                //manipulação dos bit para pegar o valor de uma cor específica
                int red = 0x0000ff & (rgb >> 16);
                int green = 0x0000ff & (rgb >> 8);
                int blue = 0x0000ff & rgb;

                //montagem das imagens de saída em canais separados
                vermelho.setRGB(i, j, (red << 16));
                verde.setRGB(i, j, (green << 8));
                azul.setRGB(i, j, blue);
            }
        }

        //lista de imagens para retorno
        ArrayList<BufferedImage> imagens = new ArrayList<>();
        imagens.add(vermelho);
        imagens.add(verde);
        imagens.add(azul);
        return imagens;
    }

    public static BufferedImage negativa(BufferedImage img) 
    {
        if (img == null) //se nada estiver aberto
        {
            return null;
        }

        //imagem de saída negativa
        BufferedImage invertida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //llop duplo para percorrer a imagem
        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                int rgb = img.getRGB(i, j); // peagando o valor RGB do píxel

                //inversão do valor de cada um dos canais dos píxels
                int blue = 255 - (0x0000ff & rgb);
                int green = 255 - (0x0000ff & (rgb >> 8));
                int red = 255 - (0x0000ff & (rgb >> 16));

                //montagem da imagem invertida de saída
                invertida.setRGB(i, j, blue | (green << 8) | (red << 16));
            }
        }

        return invertida;
    }

    public static BufferedImage equalizacaoHistograma(BufferedImage img)
    {
        if (img == null) //se nada estiver aberto ou já convertido para tons de cinza
        {
            return null;
        }

        //contagem do número de pixels de cada 
        int contagem[] = contagemTons(img);

        //valor aux constante para o cálculo de equalização
        int aux = img.getWidth() * img.getHeight();

        //imagem de saída equalizada
        BufferedImage imgRealce = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //montagem da imagem equalizada
        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                int freqAcumulada = 0; //frequencia acumulada

                int tom = img.getRGB(i, j) & 0xff; //valor rgb do pixel já convertido em Tom de cinza

                //contagem da frequencia acumulada
                for (int k = 0; k <= tom; k++)
                    freqAcumulada += contagem[k];

                //cálculo do pixel equalizado
                int valorEq = Math.max(0, Math.round(((255 * freqAcumulada) / aux)) - 1);

                //colocando o valor dentor da imagem de saída
                imgRealce.setRGB(i, j, valorEq | (valorEq << 8) | (valorEq << 16));
            }
        }

        return imgRealce;
    }

    public static int[] contagemTons(BufferedImage img) 
    {
        //vetor de contagem de valores
        int contagem[] = new int[256];
        for (int i = 0; i < 256; i++) {
            contagem[i] = 0; //preenchimento do vetor de contagem
        }
        //loop duplo para percorrer a imagem e contar os valores
        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                //pega o valor em RGB do píxel, e já converte em valor de tom de cinza
                int tom = img.getRGB(i, j) & 0xff;
                contagem[tom]++;
            }
        }

        return contagem;
    }
    
    public static BufferedImage convolucaoGenerica(BufferedImage img, float[][] mascara)
    {
        if (img == null) //se nada estiver aberto ou já convertido para tons de cinza
        {
            return null;
        }
        
        //montando a imagem de saída
        BufferedImage imgSaida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //percorrendo a imagemd e saída
        for (int i = 1; i <= (imgSaida.getWidth()-2); i++)
        {
            for (int j = 1; j <= (imgSaida.getHeight()-2); j++)
            {
                //armazena o tom do pixel
                float tom = 0;           
                
                //percorre a máscara e faz o cálculo com a máscara
                for (int k = -1; k < mascara.length-1; k++)
                {
                    for (int l = -1; l < mascara[0].length-1; l++)
                    {
                        tom += (img.getRGB(i+k, j+l) & 0xff) * mascara[k+1][l+1];
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
                int tom_int = (int)tom;
                
                //colocando na imagem de saída
                imgSaida.setRGB(i, j, tom_int | (tom_int << 8) | (tom_int << 16));
            }
        }
        return imgSaida;
    }
    
    public static BufferedImage mascaraMediana(BufferedImage img)
    {
        if (img == null) //se nada estiver aberto ou já convertido para tons de cinza
        {
            return null;
        }
        
        //motagem da imgaem de saída vazia
        BufferedImage imgSaida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //percorrendo a imagem principal
        for (int i = 1; i <= img.getWidth()-2; i++) 
        {
            for (int j = 1; j <= img.getHeight()-2; j++) 
            {
                int[] tons = new int[9]; //vetor de tons
                
                //pegando os tons da vizinhança 8 de um pixel
                tons[0] = (img.getRGB(i-1, j-1) & 0xff);
                tons[1] = (img.getRGB(i-1, j) & 0xff);
                tons[2] = (img.getRGB(i-1, j+1) & 0xff);
                tons[3] = (img.getRGB(i, j-1) & 0xff);
                tons[4] = (img.getRGB(i, j) & 0xff);
                tons[5] = (img.getRGB(i, j+1) & 0xff);
                tons[6] = (img.getRGB(i+1, j-1) & 0xff);
                tons[7] = (img.getRGB(i+1, j) & 0xff);
                tons[8] = (img.getRGB(i+1, j+1) & 0xff);
                
                //organizando os tons de forma crescente
                Arrays.sort(tons);
                
                //colocando na imagem de saída
                imgSaida.setRGB(i, j, tons[4] | (tons[4] << 8) | (tons[4] << 16));
            }
        }
        
        return imgSaida;
    }
    
    public static BufferedImage binarizacao_limiarização(BufferedImage img, int limiar)
    {
        AberturaImg abertura = AberturaImg.getInstance();
        if (abertura.getImg() == null || abertura.getImgCinza() == null) //se nada estiver aberto ou já convertido para tons de cinza
        {
            return null;
        }
        
        //motagem da imgaem de saída vazia
        BufferedImage imgSaida = new BufferedImage(abertura.getImgCinza().getWidth(), abertura.getImgCinza().getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //percorrendo a imagem principal
        for (int i = 0; i < abertura.getImgCinza().getWidth(); i++) 
        {
            for (int j = 0; j < abertura.getImgCinza().getHeight(); j++) 
            {
                //pegando o valor do pixel
                int tom = abertura.getImgCinza().getRGB(i, j) & 0xff;
                
                //se o tom do pixel for menor q o limiar passado ele fica preto, se não branco
                if (tom < limiar)
                    tom = 0;
                else 
                    tom = 255;
                
                //colcoandoa na imagem de saida
                imgSaida.setRGB(i, j, tom | (tom << 8) | (tom << 16));
            }
        }
        
        //salvando a imagem binarizada na na imagem de saida
        abertura.setImgLimiar(imgSaida);
        
        //retorno final
        return imgSaida;
    }
    
    public static double[] rgbToHSL(int rgb)
    {
        double b = (0x0000ff & rgb)/255.0f;
        double g = (0x0000ff & (rgb >> 8))/255.0f;
        double r = (0x0000ff & (rgb >> 16))/255.0f;
        
        double cmax = Math.max(r, Math.max(g, b)); // maior entre r, g, b
        double cmin = Math.min(r, Math.min(g, b)); // menor entre of r, g, b
        double diff = cmax - cmin; // diferença entre o maior e o menor
        double h = -1, s = -1;
        
        if (cmax == cmin) //se o maior e o menor forem iguais h=0
            h = 0;
 
        else if (cmax == b)
            h = (60 * ((g - b) / diff) + 360) % 360;
 
        else if (cmax == g)
            h = (60 * ((b - b) / diff) + 120) % 360;
 
        else if (cmax == b)
            h = (60 * ((b - g) / diff) + 240) % 360;
 
        if (cmax == 0)
            s = 0;
        else
            s = (diff / cmax) * 100;
        
        double l = cmax*100;
        
        double[] retorno = new double[3];
        
        retorno[0] = h;
        retorno[1] = s;
        retorno[2] = l;
        
        return retorno;
    }
}
