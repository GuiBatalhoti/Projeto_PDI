/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manipulacao;

/**
 *
 * @author Guilherme
 */
public class Conversor {
    
    public static int[] rgbToHSL(double r, double g, double b)
    {
        r /= 255.0f;
        g /= 255.0f;
        b /= 255.0f;
        
        double maior = Math.max(r, Math.max(g, b)); // maior entre r, g, b
        double menor = Math.min(r, Math.min(g, b)); // menor entre of r, g, b
        double diferenca = maior - menor; // diferença entre o maior e o menor
        double h = 0, s = 0, l = (maior + menor)/2;
        
        if (diferenca == 0)
            h = 0;
        
        else if (maior == r)
            h = ((g - b) / diferenca) % 6;
 
        else if (maior == g)
            h = (b - r) / diferenca + 2;
 
        else if (maior == b)
            h = (r - g) / diferenca + 4;
        
        h = h *40;
        
        if (h < 0)
            h += 240;
        if (diferenca != 0 )
            s = diferenca/ (1 - Math.abs(2* l -1));
        else 
           s = 0;
        
        s *= 240;
        l *= 240;      
        
        int[] retorno = new int[3];
        
        retorno[0] = (int) h;
        retorno[1] = (int) s;
        retorno[2] = (int) l;
        
        return retorno;
    }
    
    public static int[] hslToRGB(double h, double s, double l)
    {
        double r, g, b;
        s /= 240;
        l /= 240;
        
        double c = s * (1 - Math.abs(2 * l - 1));
        double x = c * (1 - Math.abs((h/40)%2 - 1));
        double m = l - c/2;
        
        if (h < 40)
        {
            r = c;
            g = x;
            b = 0;
        }
        
        else if (h < 80)
        {
            r = x;
            g = c;
            b = 0;
        }
        
        else if (h < 120)
        {
            r = 0;
            g = c;
            b = x;
        }
        
        else if (h < 160)
        {
            r = 0;
            g = x;
            b = c;
        }
        
        else if (h < 200)
        {
            r = x;
            g = 0;
            b = c;
        }
        
        else
        {
            r = c;
            g = 0;
            b = x;
        }
        
        int[] rgb = new int[3];
        
        rgb[0] = (int) ((r + m) *255);
        rgb[1] = (int) ((g + m) *255);
        rgb[2] = (int) ((b + m) *255);
        
        return rgb;
    }
}
