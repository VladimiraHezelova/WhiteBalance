package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.improvedWP;

import android.graphics.Bitmap;

import java.util.Random;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.Convertor;

/**
* Created by Vladimira Hezelova on 20. 8. 2015.
        */
public class ImprovedWP extends Convertor {

    private Bitmap originalBitmap;
    private int width;
    private int height;
    private Bitmap convertedBitmap;

    public ImprovedWP(Bitmap image) {
        this.originalBitmap = image;
        this.width = originalBitmap.getWidth();
        this.height = originalBitmap.getHeight();
        this.convertedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        conversion();
    }

    public void conversion() {

        int n = 50;
        int m = 10;

        float[] illuminationEstimation = performIlluminationEstimation(n, m);
        removeColorCast(illuminationEstimation);
    }

    public float[] performIlluminationEstimation(int n, int m) {

        float[] result = new float[]{0,0,0,0};
        float[] max = new float[]{0,0,0,0};

        float start = 0;
        float end = 1;

        int row;
        int col;

        float p1;
        float p2;

        Random randD1 = new Random();
        Random randD2 = new Random();
        float randomfloat1;
        float randomfloat2;

        for(int i = 0; i < m; i++) {
            max[0] = 0;
            max[1] = 0;
            max[2] = 0;
            for(int j = 0; j < n; j++) {
                randomfloat1 = 1 * randD1.nextFloat();
                p1 = start + (randomfloat1 * (end - start));

                randomfloat2 = 1 * randD2.nextFloat();
                p2 = start + (randomfloat2 * (end - start));

                row=(int)((height-1)*p1);
                col=(int)((width-1)*p2);

                int value = originalBitmap.getPixel(col, row);
                float[] pixelData = new float[3];
                pixelData[0] = (value >> 16) & 0xff; //red;
                pixelData[1] = (value >>  8) & 0xff; //green
                pixelData[2] = (value      ) & 0xff;  //blue

                for(int k = 0; k < 3; k++) {
                    if(max[k] < pixelData[k]) {
                        max[k] = pixelData[k];
                    }
                }
            }
            result[0] += max[0];
            result[1] += max[1];
            result[2] += max[2];

        }

        float sum = result[0]*result[0]+result[1]*result[1]+result[2]*result[2];
        sum /= 3;
        sum = (float) Math.sqrt(sum);

        result[0] /= sum;
        result[1] /= sum;
        result[2] /= sum;

        return result;
    }

    public void removeColorCast(float[] illuminationEstimation) {
        int value;
        float[] pixelData = new float[3];

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                value = originalBitmap.getPixel(j,i);
                pixelData[0] = (value >> 16) & 0xff; //red;
                pixelData[1] = (value >>  8) & 0xff; //green
                pixelData[2] = (value      ) & 0xff;  //blue

                pixelData = convert(pixelData, illuminationEstimation);
                convertedBitmap.setPixel(j, i, getValueFromRGB(pixelData));
            }
        }
    }




    public float[] convert(float[] pixelData, float[] illuminationEstimation){

        for (int k=0;k<3;++k){
            pixelData[k]/=illuminationEstimation[k];
            if(pixelData[k] > 255) {
                pixelData[k] = 255;
            }
        }
        return pixelData;
    }

    public Bitmap getConvertedBitmap() {
        return convertedBitmap;
    }
}
