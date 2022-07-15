/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulacao;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Guilherme
 */
public class FiltrosPrimeiroBim 
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
                int blue = 0xff & rgb;
                int green = 0xff & (rgb >> 8);
                int red = 0xff & (rgb >> 16);

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
                int red = 0xff & (rgb >> 16);
                int green = 0xff & (rgb >> 8);
                int blue = 0xff & rgb;

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
                int blue = 255 - (0xff & rgb);
                int green = 255 - (0xff & (rgb >> 8));
                int red = 255 - (0xff & (rgb >> 16));

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
    
    
    public static BufferedImage binarizacao(BufferedImage img)
    {
        if (img == null) //se nada estiver aberto
        {
            return null;
        }
        
        //motagem da imgaem de saída vazia
        BufferedImage imgSaida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //percorrendo a imagem principal
        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                //pegando o valor do pixel
                int tom = img.getRGB(i, j) & 0xff;
                
                //se o tom do pixel for menor q o limiar passado ele fica preto, se não branco
                if (tom < 128)
                    tom = 0;
                else 
                    tom = 255;
                
                //colcoandoa na imagem de saida
                imgSaida.setRGB(i, j, tom | (tom << 8) | (tom << 16));
            }
        }
                
        //retorno final
        return imgSaida;
    }
    
    public static BufferedImage limiarizacao(BufferedImage img, int limiar)
    {
        if (img == null) //se nada estiver aberto
        {
            return null;
        }
        
        //motagem da imgaem de saída vazia
        BufferedImage imgSaida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //percorrendo a imagem principal
        for (int i = 0; i < img.getWidth(); i++) 
        {
            for (int j = 0; j < img.getHeight(); j++) 
            {
                //pegando o valor do pixel
                int tom = img.getRGB(i, j) & 0xff;
                
                //se o tom do pixel for menor q o limiar passado ele fica preto, se não branco
                if (tom < limiar)
                    tom = 0;
                
                //colcoandoa na imagem de saida
                imgSaida.setRGB(i, j, tom | (tom << 8) | (tom << 16));
            }
        }
        
        //retorno final
        return imgSaida;
    }
    
    public static BufferedImage saltPeper(BufferedImage img)
    {
        int area = img.getWidth() * img.getHeight();
        int qtdPercent = (int) (area * 0.1f);
        
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        saida.getGraphics().drawImage(img, 0, 0, null);
        
        Random gerador = new Random();
        
        for (int i = 0; i <= qtdPercent/2; i++)
        {
            int j = gerador.nextInt(img.getWidth());
            int k = gerador.nextInt(img.getHeight());
            saida.setRGB(j, k, Color.WHITE.getRGB());
        }
        
        for (int i = 0; i <= qtdPercent/2; i++)
        {
            int j = gerador.nextInt(img.getWidth());
            int k = gerador.nextInt(img.getHeight());
            saida.setRGB(j, k, Color.BLACK.getRGB());
        }
        
        return saida;
    }
    
    public static BufferedImage escalaCompressaoDinamica(BufferedImage img, float gama, float c)
    {
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < img.getWidth(); i++)
        {
            for (int j = 0; j < img.getHeight(); j++)
            {
                int tom = img.getRGB(i, j) & 0xff;
                int novoPixel = compressao(gama, c, tom);
            
                saida.setRGB(i, j, novoPixel | (novoPixel << 8) | (novoPixel << 16));
            }
        }
        
        return saida;
    }
    
    private static int compressao(float gama, float c, int tom)
    {        
        double r = ((float)tom)/255f;
        
        double s = (c * Math.pow(r, gama));
              
        int saida = (int) (s*255);
        
        if (saida > 255)
            saida = 255;
        
        return saida;
    }
    
    
    public static BufferedImage normalizaImg(BufferedImage img)
    {
        BufferedImage saida = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        int min = 255, max = 0;
        
        for (int i = 0; i < img.getWidth(); i++)
        {
            for (int j = 0; j < img.getHeight(); j++)
            {
                int tom = img.getRGB(i, j) & 0xff;
                
                if (tom > max)
                    max = tom;
                if (tom < min)
                    min = tom;
            }
        }
        
        int aux = max - min;
        
        for (int i = 0; i < img.getWidth(); i++)
        {
            for (int j = 0; j < img.getHeight(); j++)
            {
                int tom = img.getRGB(i, j) & 0xff;
                
                int norm = (int) (255 * (tom - min) / aux );
                
                saida.setRGB(i, j, norm | (norm << 8) | (norm << 16));
            }
        }
        
        return saida;
    }
    
    public static int[][] normalizaImg(int img[][])
    {
        int saida[][] = new int[img.length][img[0].length];

        int min = img[0][0], max = img[0][0];
        
        for (int i = 0; i < img.length; i++)
        {
            for (int j = 0; j < img[0].length; j++)
            {
                int tom = img[i][j];
                
                if (tom > max)
                    max = tom;
                if (tom < min)
                    min = tom;
            }
        }
        
        for (int i = 0; i < img.length; i++)
        {
            for (int j = 0; j < img[i].length; j++)
            {
                int tom = img[i][j];
                
                int norm = (int) (255 * (tom - min) / (max - min) );
                
                saida[i][j] = norm;
            }
        }
        
        return saida;
    }
}
