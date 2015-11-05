package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch;


import android.graphics.Bitmap;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch.pixelData.PixelData;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.linearization.BackOfLinearization;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.matrixConversion.MatrixMultiplication;


/**
 * Created by Vladimira Hezelova on 15. 3. 2015.
 * <p/>
 * Image Chromatic Adapatation - White Patch (WP) Method
 */
public class Conversion {


    public static Bitmap convert(Bitmap originalImg, Bitmap realWhiteImg){

        double[][] pixelData = new double[originalImg.getWidth()*originalImg.getHeight()][3];
        // konverzia povodneho obrazu do LMS
        pixelData = ConversionToLMS.convert(PixelData.getPixelDataForRealImage(originalImg, pixelData));

        //cisla
        /*
        for(int i = 0; i < 100; i++) {
            Log.e("value1", Double.toString(pixelData[i][0]));
            Log.e("value2", Double.toString(pixelData[i][1]));
            Log.e("value3", Double.toString(pixelData[i][2]));
        }
*/
        // konverzia "realnej" bielej do LMS
        double[][] lmsRealWhite = ConversionToLMS.convert(PixelData.getPixelDataForRealWhite(realWhiteImg));

        // konverzia "idealnej" bielej do LMS
        double[][] lmsIdealWhite = ConversionToLMS.convert(new double[][]{{255,255,255}});

        double[][] scalingCoefficients;
        double kL = lmsIdealWhite[0][0] / lmsRealWhite[0][0];
        double kM = lmsIdealWhite[0][1] / lmsRealWhite[0][1];
        double kS = lmsIdealWhite[0][2] / lmsRealWhite[0][2];
        scalingCoefficients = new double[][]{{kL, 0.0, 0.0}, {0.0, kM, 0.0}, {0.0, 0.0, kS}};

        MatrixMultiplication matrixMultiplication = new MatrixMultiplication(0, 0, pixelData.length, scalingCoefficients, pixelData);
        matrixMultiplication.start();
        while (matrixMultiplication.isAlive()) {
        }
        pixelData = matrixMultiplication.getPixelData();

        // zatial bez vratenia z linearizovanej a normalizovanej podoby
        pixelData = ConversionToRGB.convert(pixelData);

        pixelData = BackOfLinearization.backOfLinearization(pixelData);

        pixelData = BackOfLinearization.backOfNormalization(pixelData);



        Bitmap resultingImg = Bitmap.createBitmap(originalImg.getWidth(), originalImg.getHeight(), Bitmap.Config.RGB_565);
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

}
