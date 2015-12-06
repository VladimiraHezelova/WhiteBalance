package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.improvedWP;

import android.graphics.Bitmap;

import java.util.Random;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;

/**
* Created by Vladimira Hezelova on 20. 8. 2015.
        */
public class ImprovedWP {
    public Bitmap conversion(int width, int height, double[][] pixelData) {

        PixelData pixelDataInstance = new PixelData();

        int n = 50;
        int m = 10;

        double[] illuminationEstimation = performIlluminationEstimation(width, height, pixelData, n, m);
        pixelData = removeColorCast(width, height, pixelData, illuminationEstimation);
        return pixelDataInstance.setBitmap(width, height, pixelData);
    }

    public double[] performIlluminationEstimation(int width, int height, double[][] pixelData, int n, int m) {

        double[] result = new double[]{0.0,0.0,0.0,0.0};
        double[] max = new double[]{0.0,0.0,0.0,0.0};

        double start = 0.0;
        double end = 1.0;

        int row;
        int col;

        double p1;
        double p2;

        Random randD = new Random();
        double randomDouble1;
        double randomDouble2;

        for(int i = 0; i < m; i++) {
            max[0] = 0.0;
            max[1] = 0.0;
            max[2] = 0.0;
            for(int j = 0; j < n; j++) {
                randomDouble1 = 1.0 * randD.nextDouble();
                p1 = start + (randomDouble1 * (end - start));

                randomDouble2 = 1.0 * randD.nextDouble();
                p2 = start + (randomDouble2 * (end - start));

             //   p1 = 0.030282793263732271;
             //   p2 = 0.90105944297145701;
             //   Log.e("p1",Double.toString(p1));
             //   Log.e("p2",Double.toString(p2));

                row=(int)((height-1)*p1);
                col=(int)((width-1)*p2);

                for(int k = 0; k < 3; k++) {
                    if(max[k] < pixelData[width*row + col][k]) {
                        max[k] = pixelData[width*row + col][k];
                    }
                }
            }
            result[0] += max[0];
            result[1] += max[1];
            result[2] += max[2];
        }

        double sum = result[0]*result[0]+result[1]*result[1]+result[2]*result[2];

        sum /= 3;
        sum = Math.sqrt(sum);

        result[0] /= sum;
        result[1] /= sum;
        result[2] /= sum;

        return result;
    }

    public double[][] removeColorCast(int width, int height, double[][] pixelData, double[] illuminationEstimation) {

        for (int i=0;i<height;++i){
            for (int j=0;j<width;++j){
                for (int k=0;k<3;++k){
                    pixelData[width*i + j][k]/=illuminationEstimation[k];
                    if(pixelData[width*i + j][k] > 255) {
                        pixelData[width*i + j][k] = 255;
                    }
                }
            }
        }
        return pixelData;
    }
}
