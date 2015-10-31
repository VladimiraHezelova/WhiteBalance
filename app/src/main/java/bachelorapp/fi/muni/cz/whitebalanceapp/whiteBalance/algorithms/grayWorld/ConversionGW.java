package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld;

import android.graphics.Bitmap;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.linearization.BackOfLinearization;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.linearization.Linearization;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.matrixConversion.MatrixMultiplication;

/**
 * Created by Vladimira Hezelova on 25. 4. 2015.
 */
public class ConversionGW {
    public static Bitmap convert(Bitmap bitmap) {
        double[][] pixelData = new double[bitmap.getWidth()*bitmap.getHeight()][3];
        System.out.println(bitmap);
        pixelData = getPixelData(bitmap, pixelData);
        System.out.println(pixelData[0][0] + " " + pixelData[0][1] + " " + pixelData[0][2]);
        Average average = new Average();
        double[][] scalingMatrix = new double[3][3];
        pixelData = Linearization.normalization(pixelData);
       // pixelData = RGBtoXYZ.fromRGBtoXYZ(pixelData);
        scalingMatrix = average.getScalingMatrix(pixelData, scalingMatrix);

        MatrixMultiplication matrixMultiplication = new MatrixMultiplication(0,0, pixelData.length, scalingMatrix, pixelData);
        matrixMultiplication.start();
        while (matrixMultiplication.isAlive()) {
           // System.out.println("vlakna su zive");
        }
        pixelData = matrixMultiplication.getPixelData();
      //  pixelData = XYZtoRGB.fromXYZtoRGB(pixelData);
        pixelData = BackOfLinearization.backOfNormalization(pixelData);

    //    Bitmap resultingImg = bitmap;
        Bitmap resultingImg = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        int height = resultingImg.getHeight();
        int width = resultingImg.getWidth();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int R = (int) pixelData[i * width + j][0];
                int G = (int) pixelData[i * width + j][1];
                int B = (int) pixelData[i * width + j][2];
                int value = ((R & 0xFF) << 16) | ((G & 0xFF) << 8)  | ((B & 0xFF));
                resultingImg.setPixel(j, i, value);
            }
        }
        return resultingImg;
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
}
