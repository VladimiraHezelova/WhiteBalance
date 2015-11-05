package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.histogramStretching;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Vladimira Hezelova on 22. 10. 2015.
 */
public class HistogramStretching {
    public static Bitmap conversion(Bitmap bitmap) {
        double[][] pixelData = new double[bitmap.getWidth()*bitmap.getHeight()][3];
        pixelData = getPixelData(bitmap, pixelData);


        pixelData = canalStretching(pixelData,0); //red

        pixelData = canalStretching(pixelData,1); //green
        pixelData = canalStretching(pixelData,2); //blue
/*
        for(int i = pixelData.length - 300; i < pixelData.length; i++) {
            for(int j = 0; j < 3; j++) {
                Log.e("pixels ", Integer.toString((int) pixelData[i][j]));
            }
        }
*/
//nuly
        return setImage(bitmap.getWidth(),bitmap.getHeight(), pixelData);
    }

    public static double[][] getPixelData(Bitmap bitmap, double[][] pixelData) {
        int value = 0;
        for(int i = 0; i < bitmap.getHeight(); i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
                value = bitmap.getPixel(j,i);
                pixelData[i*bitmap.getWidth()+j][0] = (value >> 16) & 0xff; //red;
                pixelData[i*bitmap.getWidth()+j][1] = (value >>  8) & 0xff; //green
                pixelData[i*bitmap.getWidth()+j][2] = (value      ) & 0xff;  //blue
            }
/*

            if(i > ((bitmap.getHeight() -200) )) {
                Log.e("value1", Integer.toString((int) pixelData[i][0]));
                Log.e("value2", Integer.toString((int)pixelData[i][1]));
                Log.e("value3", Integer.toString((int)pixelData[i][2]));
            }
*/
        }
        return pixelData;
    }

    public static double[][] canalStretching(double[][] pixelData, int canal) {
        int percentil = (int) (pixelData.length * 0.005);
        System.out.println("percentil" + percentil);
        ArrayList sortedPixels = getSortedPixels(pixelData, canal);



        double low = (Integer)(sortedPixels.get(percentil));
        double high = (Integer)(sortedPixels.get(pixelData.length - percentil));
        double min = 0.0;
        double max = 255;
        for(int i = 0; i < pixelData.length; i++) {
            // System.out.println("numberOfPixels " + pixelData.length);

            //  System.out.println("percentil" + percentil);
            if(pixelData[i][canal] < low) {
                pixelData[i][canal] = min;
            } else if(pixelData[i][canal] > high) {
                pixelData[i][canal] = max;
            } else {
                // System.out.println("i " + i + ", canal " + canal + ", pixelData " + pixelData[i][canal]);
                double a = ((max-min)/(high-low));
                double b = (pixelData[i][canal] - low)*((max-min)/(high-low));
                int c = (int)((pixelData[i][canal] - low)*((max-min)/(high-low)) + min);
                pixelData[i][canal] = (pixelData[i][canal] - low)*((max-min)/(high-low)) + min;
                //  System.out.println("pixelData after" + pixelData[i][canal]);
            }
        }
        //dobreeee
        /*
        for(int i = pixelData.length - 300; i < pixelData.length; i++) {
            for(int j = 0; j < 3; j++) {
                Log.e("pixelData.length", Integer.toString(pixelData.length));
                Log.e("pixels ", Integer.toString((int) pixelData[i][j]));
            }
        }
        */

        return pixelData;
    }

    public static ArrayList<Integer> getSortedPixels(double[][] pixelData, int canal) {
        ArrayList array = new ArrayList<Integer>();
        for(int i = 0; i < pixelData.length; i++) {
            Integer integer = (int) pixelData[i][canal];
            array.add(integer);
        }



            Collections.sort(array, new Comparator<Integer>() {

                @Override
                public int compare(Integer entry1, Integer entry2) {
                    return entry1.compareTo(entry2);
                }
            });



        return array;
    }

    public static Bitmap setImage(int width, int height, double[][] pixelData) {

// nuly soourceImage
        /*
        for(int i = sourceImage.getHeight() - 100; i < sourceImage.getHeight(); i++) {
            for(int j = sourceImage.getWidth() - 100; j < sourceImage.getWidth(); j++) {
                Log.e("pixels ", Integer.toString(sourceImage.getPixel(j, i)));
            }
        }
        */

        //dobree
/*
        for(int i = pixelData.length - 300; i < pixelData.length; i++) {
            for(int j = 0; j < 3; j++) {
                Log.e("pixels ", Integer.toString((int) pixelData[i][j]));
            }
        }

*/

        Bitmap histogramStretchedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int R = (int) pixelData[i * width + j][0];
                //dobree
                /*
                if (i > height - 100 && j > width - 100) {
                    Log.e("R ", Integer.toString(R));
                }
                */
                int G = (int) pixelData[i * width + j][1];
                int B = (int) pixelData[i * width + j][2];
                int value = ((R & 0xFF) << 16) | ((G & 0xFF) << 8)  | ((B & 0xFF) << 0);


                histogramStretchedBitmap.setPixel(j, i, value);

            }
        }
        //nuly

/*
        for(int i = sourceImage.getHeight() - 100; i < sourceImage.getHeight(); i++) {
            for(int j = sourceImage.getWidth() - 100; j < sourceImage.getWidth(); j++) {
                Log.e("pixels ", Integer.toString(sourceImage.getPixel(j, i)));
            }
        }
*/
        return histogramStretchedBitmap;
    }
}
