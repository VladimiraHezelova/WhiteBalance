package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch.pixelData;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by Vladimira Hezelova on 23. 3. 2015.
 */
public class PixelData {

    public static double[][] getPixelDataForRealImage(Bitmap img, double[][] lmsOriginalImg) {
        return getPixelData(img, lmsOriginalImg);
    }

    public static double[][] getPixelData(Bitmap bitmap, double[][] pixelData) {
        int value;
        for(int i = 0; i < bitmap.getHeight(); i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
                value = bitmap.getPixel(j,i);
                pixelData[i*bitmap.getWidth()+j][0] = (value >> 16) & 0xff; //red;
                pixelData[i*bitmap.getWidth()+j][1] = (value >>  8) & 0xff; //green
                pixelData[i*bitmap.getWidth()+j][2] = (value      ) & 0xff;  //blue
            }
//hodnoty
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

    public static double[][] getPixelDataForRealWhite(Bitmap img) {
        double rgb[] = new double[3];
        return new double[][]{getPixelDataFromValue(median(img), rgb)};
    }

    private static int median(Bitmap img) {
        int height = img.getHeight();
        int width = img.getWidth();

        int[] m = new int[height*width];

        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                m[i * width + j] = img.getPixel(j, i);
            }
        }
        Arrays.sort(m);

        int middle = m.length/2;
        if (m.length%2 == 1) {
            return m[middle];
        } else {
            return (m[middle-1] + m[middle]) / 2;
        }
    }

    public static double[] getPixelDataFromValue(int value, double rgb[]) {
        rgb[0] = (value >> 16) & 0xff; //red
        rgb[1] = (value >>  8) & 0xff; //green
        rgb[2] = (value      ) & 0xff;  //blue
        return rgb;
    }
}
