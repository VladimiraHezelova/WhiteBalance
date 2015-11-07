package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.histogramStretching;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class HistogramStretching {
    public static Bitmap conversion(int width, int height, double[][] pixelData) {

        pixelData = canalStretching(pixelData,0); //red
        pixelData = canalStretching(pixelData,1); //green
        pixelData = canalStretching(pixelData,2); //blue

        return PixelData.setBitmap(width, height, pixelData);
    }

    public static double[][] canalStretching(double[][] pixelData, int canal) {
        int percentil = (int) (pixelData.length * 0.005);
        ArrayList sortedPixels = getSortedPixels(pixelData, canal);

        double low = (Integer)(sortedPixels.get(percentil));
        double high = (Integer)(sortedPixels.get(pixelData.length - percentil));
        double min = 0.0;
        double max = 255;
        for(int i = 0; i < pixelData.length; i++) {
            if(pixelData[i][canal] < low) {
                pixelData[i][canal] = min;
            } else if(pixelData[i][canal] > high) {
                pixelData[i][canal] = max;
            } else {
                pixelData[i][canal] = (pixelData[i][canal] - low)*((max-min)/(high-low)) + min;
            }
        }
        return pixelData;
    }

    public static ArrayList<Integer> getSortedPixels(double[][] pixelData, int canal) {
        ArrayList array = new ArrayList<>();
        Integer integer;
        for (double[] aPixelData : pixelData) {
            integer = (int) aPixelData[canal];
            array.add(integer);
        }
        Collections.sort(array);

        return array;
    }
}
