package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.histogramStretching;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Arrays;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class HistogramStretching {
    public Bitmap conversion(int width, int height, double[][] pixelData) {

        PixelData pixelDataInstance = new PixelData();

        /** The number of distinct short values. */
        final int NUM_SHORT_VALUES = 1 << 16;
        // Use counting sort on huge arrays
        int[] count = new int[NUM_SHORT_VALUES];

        short[] sortedPixels = new short[pixelData.length];
        pixelData = canalStretching(pixelData,0, sortedPixels, count); //red
        Arrays.fill(count, 0);

        pixelData = canalStretching(pixelData,1, sortedPixels, count); //green
        Arrays.fill(count, 0);
        pixelData = canalStretching(pixelData,2, sortedPixels, count); //blue

        return pixelDataInstance.setBitmap(width, height, pixelData);
    }

    public double[][] canalStretching(double[][] pixelData, int canal, short[] sortedPixels, int[] count) {
        int percentil = (int) (pixelData.length * 0.005);
        sortedPixels = getSortedPixels(pixelData, canal, sortedPixels, count);

        double low = sortedPixels[percentil];
        double high = sortedPixels[pixelData.length - percentil];
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

    public short[] getSortedPixels(double[][] pixelData, int canal, short[] sortedPixels, int[] count) {
        for(int i = 0; i < pixelData.length; i++) {
            sortedPixels[i] = (short)pixelData[i][canal];
        }
      //  doSort(sortedPixels, 0, sortedPixels.length - 1);
        doSort(sortedPixels, 0, sortedPixels.length-1, count);
        return sortedPixels;
    }

    /**
     * Sorts the specified range of the array into ascending order. This
     * method differs from the public {@code sort} method in that the
     * {@code right} index is inclusive, and it does no range checking on
     * {@code left} or {@code right}.
     *
     * @param a the array to be sorted
     * @param left the index of the first element, inclusive, to be sorted
     * @param right the index of the last element, inclusive, to be sorted
     */
    private static void doSort(short[] a, int left, int right, int[] count) {
        /**
         * If the length of an array to be sorted is less than this
         * constant, insertion sort is used in preference to Quicksort.
         */
        final int INSERTION_SORT_THRESHOLD = 32;
        /**
         * If the length of a short or char array to be sorted is greater
         * than this constant, counting sort is used in preference to Quicksort.
         */
        final int COUNTING_SORT_THRESHOLD_FOR_SHORT_OR_CHAR = 32768;

        // Use insertion sort on tiny arrays
        if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
            for (int i = left + 1; i <= right; i++) {
                short ai = a[i];
                int j;
                for (j = i - 1; j >= left && ai < a[j]; j--) {
                    a[j + 1] = a[j];
                }
                a[j + 1] = ai;
            }
        } else if (right-left+1 > COUNTING_SORT_THRESHOLD_FOR_SHORT_OR_CHAR) {
            // Use counting sort on huge arrays
         //   int[] count = new int[NUM_SHORT_VALUES];

            for (int i = left; i <= right; i++) {
                count[a[i] - Short.MIN_VALUE]++;
            }
            for (int i = 0, k = left; i < count.length && k <= right; i++) {
                short value = (short) (i + Short.MIN_VALUE);

                for (int s = count[i]; s > 0; s--) {
                    a[k++] = value;
                }
            }
        } else { // Use Dual-Pivot Quicksort on large arrays
            Log.e("dualPivotQuicksort", "dopis");
          //  dualPivotQuicksort(a, left, right);
        }
    }

}
