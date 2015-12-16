package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.histogramStretching;

import android.graphics.Bitmap;
import android.util.Log;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.Convertor;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class HistogramStretching extends Convertor {

    private int low[];
    private int high[];
    private float min;
    private float max;


    private Bitmap originalBitmap;

    public HistogramStretching(Bitmap bitmap) {
        super(bitmap);
        this.originalBitmap = bitmap;
        this.min = 0;
        this.max = 255;
        this.low = new int[3];
        this.high = new int[3];
        setScalingCoefficients();
        balanceWhite();
    }

    public void findBoundary() {
        int[][] histogram = getHistogram();

        long start = System.currentTimeMillis();

        int percentil = (int) (originalBitmap.getWidth()*originalBitmap.getHeight() * 0.05);
        int intensity = 0;
        int number = 0;

        for(int canal = 0; canal < 3; canal++) {
            while(number < percentil) {
                number += histogram[canal][intensity];
                intensity++;
            }
            low[canal] = intensity-1;

            intensity = 255;
            number = 0;

            while(number < percentil) {
                number += histogram[canal][intensity];
                intensity--;
            }
            high[canal] = intensity+1;

            intensity = 0;
            number = 0;
        }


        long end = System.currentTimeMillis();
        double time = (double) (end - start) / 1000;
        Log.i("Find boundary", "time of conversions = " + time + "seconds");
    }

    public void setScalingCoefficients() {

        long start = System.currentTimeMillis();


        findBoundary();

        long end = System.currentTimeMillis();
        double time = (double) (end - start) / 1000;
        Log.i("setScalingCoefficients", "time of conversions = " + time + "seconds");
    }

    public int[][] getHistogram() {
        long start = System.currentTimeMillis();

        int[][] histogram = new int[3][256];

        int value;
        int intensity[] = new int[3];
        int height = originalBitmap.getHeight();
        int width = originalBitmap.getWidth();

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(originalBitmap != null && !originalBitmap.isRecycled()) {
                    value = originalBitmap.getPixel(j,i);

                    intensity[0] = (value >> 16) & 0xff; //red
                    intensity[1] = (value >>  8) & 0xff; //green
                    intensity[2] = (value      ) & 0xff;  //blue

                    histogram[0][intensity[0]]++;
                    histogram[1][intensity[1]]++;
                    histogram[2][intensity[2]]++;

                    //  Log.e("intensity", Integer.toString(intensity));
                    //  Log.e("number", Integer.toString(number));

                }
            }
        }

        long end = System.currentTimeMillis();
        double time = (double) (end - start) / 1000;
        Log.i("getHistogran", "time of conversions = " + time + "seconds");

        return histogram;
    }

    @Override
    public float[] removeColorCast(float[] pixelData, float[] outRGB) {

        for(int i = 0; i < 3; i++) {
            if(pixelData[i] < low[i]) {
                pixelData[i] = min;
            } else if(pixelData[i] > high[i]) {
                pixelData[i] = max;
            } else {
                pixelData[i] = (pixelData[i] - low[i]) * (max - min) / (high[i] - low[i]) + min;
            }
        }
        return pixelData;
    }
}
