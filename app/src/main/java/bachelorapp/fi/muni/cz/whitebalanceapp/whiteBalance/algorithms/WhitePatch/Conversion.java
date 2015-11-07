package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch;


import android.graphics.Bitmap;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.Linearization;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.MatrixMultiplication;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;


/**
 * Created by Vladimira Hezelova on 15. 3. 2015.
 * <p/>
 * Image Chromatic Adapatation - White Patch (WP) Method
 */
public class Conversion {


    public static Bitmap convert(Bitmap originalImg, Bitmap realWhiteImg , double[][] pixelData){


        // konverzia povodneho obrazu do LMS

        pixelData = conversionsToLMS(pixelData);

        // konverzia "realnej" bielej do LMS
        //  double[][] lmsRealWhite = conversionsToLMS(PixelData.getPixelDataForRealWhite(realWhiteImg));
        double[] rgb = new double[3];
        double[][] pixelDataForRealWhite = new double[][]{PixelData.getPixelDataFromValue(PixelData.median(realWhiteImg), rgb)};
        double[][] lmsRealWhite = conversionsToLMS(pixelDataForRealWhite);



        // konverzia "idealnej" bielej do LMS
        double[][] lmsIdealWhite = conversionsToLMS(new double[][]{{255, 255, 255}});

        double[][] scalingCoefficients;
        double kL = lmsIdealWhite[0][0] / lmsRealWhite[0][0];
        double kM = lmsIdealWhite[0][1] / lmsRealWhite[0][1];
        double kS = lmsIdealWhite[0][2] / lmsRealWhite[0][2];
        scalingCoefficients = new double[][]{{kL, 0.0, 0.0}, {0.0, kM, 0.0}, {0.0, 0.0, kS}};

        pixelData = MatrixMultiplication.multiply(scalingCoefficients, pixelData);

        // zatial bez vratenia z linearizovanej a normalizovanej podoby
        pixelData = MatrixMultiplication.fromLMStoRGB(pixelData);
        pixelData = Linearization.nonLinearize(pixelData);
        pixelData = Linearization.nonNormalize(pixelData);


        return PixelData.setBitmap(originalImg.getWidth(), originalImg.getHeight(), pixelData);
    }

    public static double[][] conversionsToLMS(double[][] pixelData) {
        pixelData = Linearization.normalize(pixelData);
        pixelData = Linearization.linearize(pixelData);
        return MatrixMultiplication.fromRGBtoLMS(pixelData);
    }

}
