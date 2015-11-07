package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.histogramStretching;

import android.graphics.Bitmap;

import java.util.Arrays;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class HistogramStretching {
    public static Bitmap conversion(int width, int height, double[][] pixelData) {

        double[] sortedPixels = new double[pixelData.length];
        pixelData = canalStretching(pixelData,0, sortedPixels); //red
        pixelData = canalStretching(pixelData,1, sortedPixels); //green
        pixelData = canalStretching(pixelData,2, sortedPixels); //blue

        return PixelData.setBitmap(width, height, pixelData);
    }

    public static double[][] canalStretching(double[][] pixelData, int canal, double[] sortedPixels) {
        int percentil = (int) (pixelData.length * 0.005);
        sortedPixels = getSortedPixels(pixelData, canal, sortedPixels);

        double low = sortedPixels[percentil];
        double high = sortedPixels[pixelData.length - percentil];
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

    public static double[] getSortedPixels(double[][] pixelData, int canal, double[] sortedPixels) {
        /*
        ArrayList array = new ArrayList<>();
        Integer integer;
        for(int i = 0; i < pixelData.length; i++) {
            integer = (int) pixelData[i][canal];
            array.add(integer);
        }
        Collections.sort(array);
*/

        for(int i = 0; i < pixelData.length; i++) {
            sortedPixels[i] = pixelData[i][canal];
        }
        Arrays.sort(sortedPixels);

        return sortedPixels;
    }
}
