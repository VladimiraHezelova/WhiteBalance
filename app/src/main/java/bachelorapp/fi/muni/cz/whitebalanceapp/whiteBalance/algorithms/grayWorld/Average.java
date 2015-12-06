package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld;

import android.graphics.Bitmap;

/**
 * Created by Vladimira Hezelova on 25. 4. 2015.
 */
public class Average {

    private float avgR;
    private float avgG;
    private float avgB;
    private float avgGray;
    private float kR;
    private float kG;
    private float kB;

    private Bitmap bitmap;
    private int width;
    private int height;

    public Average(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public float[][] getScalingMatrix(float[][] scalingMatrix) {
        getAverages();
        getAvgGray();
        getScalingCoefficients();

        scalingMatrix[0][0] = kR;
        scalingMatrix[1][1] = kG;
        scalingMatrix[2][2] = kB;

        return scalingMatrix;
    }

    private void getAverages() {
        float sumR = 0;
        float sumG = 0;
        float sumB = 0;
        int value;
        float[] pixelData = new float[3];

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                value = bitmap.getPixel(j,i);
                pixelData[0] = (value >> 16) & 0xff; //red;
                pixelData[1] = (value >>  8) & 0xff; //green
                pixelData[2] = (value      ) & 0xff;  //blue
                sumR += pixelData[0];
                sumG += pixelData[1];
                sumB += pixelData[2];
            }
        }
        float numberOfPixels = pixelData.length;
        avgR = sumR / numberOfPixels;
        avgG = sumG / numberOfPixels;
        avgB = sumB / numberOfPixels;
    }

    private void getAvgGray() {
        avgGray = 0.299f * avgR + 0.587f * avgG + 0.114f * avgB;
    }

    private void getScalingCoefficients() {
        kR = avgGray / avgR;
        kG = avgGray / avgG;
        kB = avgGray / avgB;
    }



}
