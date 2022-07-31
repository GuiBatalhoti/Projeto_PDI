/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manipulacao.afinamento;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.LinkedList;

/**
 *
 * @author Guilherme
 */
public class ZhangSuen {
    
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    private static int[][] imgToMatrix(BufferedImage img)
    {
        int[][] saida = new int[img.getWidth()][img.getHeight()];
        
        for (int i = 0; i< img.getWidth(); i++)
        {
            for (int j = 0; j <img.getHeight(); j++)
            {
                saida[i][j] = (img.getRGB(i, j) & 255)/255;
            }
        }
        
        return saida;
    }
    
    private static BufferedImage matrixToImg(int[][] matrix){
        BufferedImage saida = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                Color cor = new Color(matrix[i][j] * 255, matrix[i][j] * 255, matrix[i][j] * 255);
                saida.setRGB(i, j, cor.getRGB());
            }
        }
        
        return saida;
    }
    
    public static BufferedImage afinamento(BufferedImage img) {
        
        int[][] imgBinaria = imgToMatrix(img);
                      
        int transicoes, vizinhos, mult01, mult02;
        LinkedList<Ponto> pontosAApagar = new LinkedList();
        
        boolean hasChange;
        
        do {
            hasChange = false;
            
            for (int i = 1; i + 1 < imgBinaria.length; i++) {
                for (int j = 1; j + 1 < imgBinaria[i].length; j++) {
                    transicoes = nTransicoes(imgBinaria, i, j);
                    vizinhos = nVizinhos(imgBinaria, i, j);
                    mult01 = mult01_01(imgBinaria, i, j);
                    mult02 = mult02_01(imgBinaria, i, j);
                    
                    if (imgBinaria[i][j] == 1 && 2 <= vizinhos && vizinhos <= 6 && transicoes == 1 && (mult01 == 0) && (mult02 == 0)) {
                        
                        pontosAApagar.add(new Ponto(j, i));
                        hasChange = true;
                    }
                }
            }
            
            for (Ponto point : pontosAApagar) {
                imgBinaria[point.getY()][point.getX()] = 0;
            }
            pontosAApagar.clear();
            
            for (int i = 1; i + 1 < imgBinaria.length; i++) {
                for (int j = 1; j + 1 < imgBinaria[i].length; j++) {
                    transicoes = nTransicoes(imgBinaria, i, j);
                    vizinhos = nVizinhos(imgBinaria, i, j);
                    mult01 = mult01_02(imgBinaria,i , j);
                    mult02 = mult02_02(imgBinaria, i ,j);
                    
                    if (imgBinaria[i][j] == 1 && 2 <= vizinhos && vizinhos <= 6 && transicoes == 1 && (mult01 == 0) && (mult02 == 0)) {
                        pontosAApagar.add(new Ponto(j, i));
                        hasChange = true;
                    }
                }
            }
            
            for (Ponto point : pontosAApagar) {
                imgBinaria[point.getY()][point.getX()] = 0;
            }
            
            pontosAApagar.clear();
            
        } while (hasChange);
        
        return matrixToImg(imgBinaria);
    }
 
    private static int nTransicoes(int[][] imgBinaria, int i, int j) {
        int count = 0;
//p2 p3
        if (i - 1 >= 0 && j + 1 < imgBinaria[i].length && imgBinaria[i - 1][j] == 0 && imgBinaria[i - 1][j + 1] == 1) {
            count++;
        }
//p3 p4
        if (i - 1 >= 0 && j + 1 < imgBinaria[i].length && imgBinaria[i - 1][j + 1] == 0 && imgBinaria[i][j + 1] == 1) {
            count++;
        }
//p4 p5
        if (i + 1 < imgBinaria.length && j + 1 < imgBinaria[i].length && imgBinaria[i][j + 1] == 0 && imgBinaria[i + 1][j + 1] == 1) {
            count++;
        }
//p5 p6
        if (i + 1 < imgBinaria.length && j + 1 < imgBinaria[i].length && imgBinaria[i + 1][j + 1] == 0 && imgBinaria[i + 1][j] == 1) {
            count++;
        }
//p6 p7
        if (i + 1 < imgBinaria.length && j - 1 >= 0 && imgBinaria[i + 1][j] == 0 && imgBinaria[i + 1][j - 1] == 1) {
            count++;
        }
//p7 p8
        if (i + 1 < imgBinaria.length && j - 1 >= 0 && imgBinaria[i + 1][j - 1] == 0 && imgBinaria[i][j - 1] == 1) {
            count++;
        }
//p8 p9
        if (i - 1 >= 0 && j - 1 >= 0 && imgBinaria[i][j - 1] == 0 && imgBinaria[i - 1][j - 1] == 1) {
            count++;
        }
//p9 p2
        if (i - 1 >= 0 && j - 1 >= 0 && imgBinaria[i - 1][j - 1] == 0 && imgBinaria[i - 1][j] == 1) {
            count++;
        }
        return count;
    }
 
    private static int nVizinhos(int[][] imgBinaria, int y, int x) {
                
        return imgBinaria[y - 1][x] + imgBinaria[y - 1][x + 1] + imgBinaria[y][x + 1]
                + imgBinaria[y + 1][x + 1] + imgBinaria[y + 1][x] + imgBinaria[y + 1][x - 1]
                + imgBinaria[y][x - 1] + imgBinaria[y - 1][x - 1];
    }
    
    private static int mult01_01(int[][] imgBinaria, int i, int j)
    {
        return imgBinaria[i - 1][j] * imgBinaria[i][j + 1] * imgBinaria[i + 1][j];
    }
    
    private static int mult02_01(int[][] imgBinaria, int i, int j)
    {
        return imgBinaria[i][j + 1] * imgBinaria[i + 1][j] * imgBinaria[i][j - 1];
    }
    
    private static int mult01_02(int[][] imgBinaria, int i, int j)
    {
        return imgBinaria[i - 1][j] * imgBinaria[i][j + 1] * imgBinaria[i][j - 1];
    }
    
    private static int mult02_02(int[][] imgBinaria, int i, int j)
    {
        return imgBinaria[i - 1][j] * imgBinaria[i + 1][j] * imgBinaria[i][j - 1];
    }
    
    /*
    public static BufferedImage afinamento(BufferedImage img)
    {
        BufferedImage saida;
        saida = deepCopy(img);
                
        LinkedList<Ponto> pontosAApagar;
        Boolean mudou;
        
        do
        {
            mudou = false;
            
            pontosAApagar = marcaPontos01(saida);
            if (!pontosAApagar.isEmpty())
                mudou = true;
            
            saida = apagaPontos(pontosAApagar, saida);
            
            pontosAApagar.clear();

            pontosAApagar = marcaPontos02(saida);
            if (!pontosAApagar.isEmpty())
                mudou = true;
            
            saida = apagaPontos(pontosAApagar, saida);
            
            pontosAApagar.clear();

        }while(mudou);
        
        return saida;        
    }
    
    private static LinkedList marcaPontos01(BufferedImage img)
    {
        LinkedList<Ponto> pontosAApagar = new LinkedList();
        
        for (int i = 1; i < img.getWidth() - 1; i++)
        {
            for (int j = 1; j < img.getHeight() - 1; j++)
            {
                int cond1 = nVizinhos(i, j, img);
                int cond2 = nTransicoes(i, j, img);
                int cond3 = condicao03_01(i, j, img);
                int cond4 = condicao04_01(i, j, img);
                
                if ( ((img.getRGB(i, j) & 255) == 255) && (2 <= cond1 &&  cond1 <= 6) && (cond2 == 1) && (cond3 == 0) && (cond4 == 0))
                {
                    pontosAApagar.add(new Ponto(i, j));
                }
            }
        }
        
        return pontosAApagar;
    }
    
    private static LinkedList marcaPontos02(BufferedImage img)
    {
        LinkedList<Ponto> pontosAApagar = new LinkedList();
        
        for (int i = 1; i < img.getWidth() - 1; i++)
        {
            for (int j = 1; j < img.getHeight() - 1; j++)
            {
                int cond1 = nVizinhos(i, j, img);
                int cond2 = nTransicoes(i, j, img);
                int cond3 = condicao03_02(i, j, img);
                int cond4 = condicao04_02(i, j, img);
                
                if ( ((img.getRGB(i, j) & 255) == 255) && (cond1 >= 2 && cond1 <= 6) && (cond2 == 1) && (cond3 == 0) && (cond4 == 0))
                {
                    pontosAApagar.add(new Ponto(i, j));
                }
            }
        }
        
        return pontosAApagar;
    }
    
    private static int nVizinhos(int i, int j, BufferedImage img)
    {
        int count = 0;
        
        int[][] vizinhos = new int[3][3];
        
        for (int x = -1; x < 2; x++)
        {
            for (int y = -1; y < 2; y++)
            {
                int tom = img.getRGB(i + x, j + y) & 255;
                
                vizinhos[x+1][y+1] = tom/255;                
            }
        }
        
        count = vizinhos[0][0] + vizinhos[0][1] + vizinhos[0][2] +
                vizinhos[1][0] + 0              + vizinhos[1][2] +
                vizinhos[2][0] + vizinhos[2][1] + vizinhos[2][2];
        
        return count;
    }

    private static int nTransicoes(int i, int j, BufferedImage img) 
    {
        int count = 0;        
        
        //p2 e p3
        if ( (i - 1 >= 0) && (j + 1 < img.getHeight()) && ( (img.getRGB(i - 1, j) & 255) == 0) && ( (img.getRGB(i - 1, j + 1) & 255) == 255) )
            count++;
        
        //p3 e p4
        if ( (i - 1 >= 0) && (j + 1 < img.getHeight()) && ( (img.getRGB(i - 1, j + 1) & 255) == 0) && ( (img.getRGB(i, j + 1) & 255) == 255) )
            count++;
        
        //p4 e p5
        if ( (i + 1 < img.getWidth()) && (j + 1 <= img.getHeight()) && ( (img.getRGB(i, j + 1) & 255) == 0) && ( (img.getRGB(i + 1, j + 1) & 255) == 255) )
            count++;
        
        //p5 e p6
        if ( (i + 1 < img.getWidth()) && (j + 1 <= img.getHeight()) && ( (img.getRGB(i + 1, j + 1) & 255) == 0) && ( (img.getRGB(i + 1, j) & 255) == 255) )
            count++;
        
        //p6 e p7
        if ( (i + 1 < img.getWidth()) && (j - 1 >= 0) && ( (img.getRGB(i + 1, j) & 255) == 0) && ( (img.getRGB(i + 1, j - 1) & 255) == 255) )
            count++;
        
        //p7 e p8
        if ( (i + 1 < img.getWidth()) && (j - 1 >= 0) && ( (img.getRGB(i + 1, j - 1) & 255) == 0) && ( (img.getRGB(i, j + 1) & 255) == 255) )
            count++;
        
        //p8 e p9
        if ( (i - 1 >= 0) && (j - 1 >= 0) && ( (img.getRGB(i, j + 1) & 255) == 0) && ( (img.getRGB(i - 1, j - 1) & 255) == 255) )
            count++;
        
        //p9 e p2
        if ( (i - 1 >= 0) && (j - 1 >= 0) && ( (img.getRGB(i - 1, j - 1) & 255) == 0) && ( (img.getRGB(i - 1, j) & 255) == 255) )
            count++;
        
        return count;
    }
    */
}
