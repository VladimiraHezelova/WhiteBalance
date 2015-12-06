package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.histogramStretching;

import android.graphics.Bitmap;
import android.util.SparseIntArray;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class HistogramStretching {

    private int low;
    private int high;
    private Bitmap originalBitmap;

    public HistogramStretching(Bitmap bitmap) {
        this.originalBitmap = bitmap;
    }

    public void findBoundary(Bitmap bitmap, int canal) {
        SparseIntArray histogram = getHistogram(bitmap, canal);
        int percentil = (int) (bitmap.getWidth()*bitmap.getHeight() * 0.05);
        int intensity = 0;
        int number = 0;
        while(number < percentil) {
            number += histogram.get(intensity);
            intensity++;
        }
        low = intensity-1;

        intensity = 255;
        number = 0;
        while(number < percentil) {
            number += histogram.get(intensity);
            intensity--;
        }
        high = intensity+1;
    }


    public Bitmap conversion(int width, int height, double[][] pixelData) {

        PixelData pixelDataInstance = new PixelData();

        pixelData = canalStretching(pixelData,0); //red
        pixelData = canalStretching(pixelData,1); //green
        pixelData = canalStretching(pixelData,2); //blue

        return pixelDataInstance.setBitmap(width, height, pixelData);
    }

    public double[][] canalStretching(double[][] pixelData, int canal) {

        findBoundary(originalBitmap, canal);

        double min = 0.0;
        double max = 255;
        double a = max-min;
        double b = high - low;
        double c = a / b;
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


    public SparseIntArray getHistogram(Bitmap bitmap, int canal) {
        SparseIntArray histogram = new SparseIntArray();
        for(int i = 0; i < 256; i++) {
            histogram.put(i,0);
        }
        int value;
        int intensity = 0;
        for(int i = 0; i < bitmap.getHeight(); i++) {
            for(int j = 0; j < bitmap.getWidth(); j++) {
                value = bitmap.getPixel(j,i);
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
        return histogram;

    }

}
