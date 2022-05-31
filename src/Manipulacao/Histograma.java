/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulacao;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
/**
 *
 * @author Guilherme
 */
public class Histograma {
    
    //imagem para fazer o histograma
    private static BufferedImage img;

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        Histograma.img = img;
    }
    
    //manipulaçoes para o gráfico
    public HistogramDataset createDataSet(int contagem[]) {
        
        //data set de histograma
        HistogramDataset dataset = new HistogramDataset();
        double[] aux = Arrays.stream(contagem).asDoubleStream().toArray(); //conversão para vetor de doubles
        dataset.addSeries("", aux, contagem.length); //criação do data set

        return dataset;
    }

    public JFreeChart createBarChart(HistogramDataset dataset) {
        //motnagem do gráfico
        JFreeChart grafico = ChartFactory.createHistogram("Histograma", null, null, dataset, PlotOrientation.VERTICAL, false, true, true);
        return grafico;
    }

    public JFreeChart criaGrafico(int contagem[]) {

        JFreeChart grafico = this.createBarChart(this.createDataSet(contagem));
        return grafico;
    }
}
