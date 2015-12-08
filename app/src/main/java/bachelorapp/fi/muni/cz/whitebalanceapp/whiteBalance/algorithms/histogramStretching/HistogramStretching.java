package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.histogramStretching;

import android.graphics.Bitmap;
import android.util.SparseIntArray;

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

    public void findBoundary(int canal) {
        SparseIntArray histogram = getHistogram(canal);
        int percentil = (int) (originalBitmap.getWidth()*originalBitmap.getHeight() * 0.05);
        int intensity = 0;
        int number = 0;
        while(number < percentil) {
            number += histogram.get(intensity);
            intensity++;
        }
        low[canal] = intensity-1;

        intensity = 255;
        number = 0;
        while(number < percentil) {
            number += histogram.get(intensity);
            intensity--;
        }
        high[canal] = intensity+1;
    }

    public void setScalingCoefficients() {
        for(int i = 0; i < 3; i++) {
            findBoundary(i);
            findBoundary(i);
        }
    }

/*
    public Bitmap conversion(int width, int height, float[][] pixelData) {

        PixelData pixelDataInstance = new PixelData();

        pixelData = canalStretching(pixelData,0); //red
        pixelData = canalStretching(pixelData,1); //green
        pixelData = canalStretching(pixelData,2); //blue

        return pixelDataInstance.setBitmap(width, height, pixelData);
    }
    */
/*
    public float[][] canalStretching(float[][] pixelData, int canal) {

        findBoundary(originalBitmap, canal);


        float a = max-min;
        float b = high - low;
        float c = a / b;
        for(int i = 0; i < pixelData.length; i++) {
            if(pixelData[i][canal] < low) {
                pixelData[i][canal] = min;
            } else if(pixelData[i][canal] > high) {
                pixelData[i][canal] = max;
            } else {
                pixelData[i][canal] = (pixelData[i][canal] - low)*c + min;
            }
        }
        return pixelData;
    }
*/

    public SparseIntArray getHistogram(int canal) {
        SparseIntArray histogram = new SparseIntArray();
        for(int i = 0; i < 256; i++) {
            histogram.put(i,0);
        }
        int value;
        int intensity = 0;
        for(int i = 0; i < originalBitmap.getHeight(); i++) {
            for(int j = 0; j < originalBitmap.getWidth(); j++) {
                if(originalBitmap != null && !originalBitmap.isRecycled()) {
                    value = originalBitmap.getPixel(j,i);
                    switch(canal) {
                        case 0: intensity = (value >> 16) & 0xff; //red
                            break;
                        case 1: intensity = (value >>  8) & 0xff; //green
                            break;
                        case 2: intensity = (value      ) & 0xff;  //blue
                            break;
                    }
                    int number = histogram.get(intensity);
                    //  Log.e("intensity", Integer.toString(intensity));
                    //  Log.e("number", Integer.toString(number));
                    number++;
                    histogram.put(intensity, number);
                }
            }
        }
        return histogram;
    }

    @Override
    public float[] removeColorCast(float[] pixelData) {

        for(int i = 0; i < 3; i++) {
            if(pixelData[i] < low[i]) {
                pixelData[i] = min;
            } else if(pixelData[i] > high[i]) {
                pixelData[i] = max;
            } else {
                float a = max - min;
                float b = high[i] - low[i];
                float c = a / b;
                pixelData[i] = (pixelData[i] - low[i])*c + min;
            }
        }
        return pixelData;
    }
}
