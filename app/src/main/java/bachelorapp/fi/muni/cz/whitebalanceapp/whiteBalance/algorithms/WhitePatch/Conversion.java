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
    public Bitmap convert(int width, int height, Bitmap realWhiteImg, double[][] pixelData){

        PixelData pixelDataInstance = new PixelData();
        MatrixMultiplication matrixMultiplicationInstance = new MatrixMultiplication();
        Linearization linearizationInstance = new Linearization();
        // konverzia povodneho obrazu do LMS

        pixelData = conversionsToLMS(pixelData, matrixMultiplicationInstance, linearizationInstance);

        // konverzia "realnej" bielej do LMS
        //  double[][] lmsRealWhite = conversionsToLMS(PixelData.getPixelDataForRealWhite(realWhiteImg));
        double[] rgb = new double[3];
        double[][] pixelDataForRealWhite = new double[][]{pixelDataInstance.getPixelDataFromValue(pixelDataInstance.median(realWhiteImg), rgb)};
        double[][] lmsRealWhite = conversionsToLMS(pixelDataForRealWhite, matrixMultiplicationInstance, linearizationInstance);



        // konverzia "idealnej" bielej do LMS
        double[][] lmsIdealWhite = conversionsToLMS(new double[][]{{255, 255, 255}}, matrixMultiplicationInstance, linearizationInstance);

        double[][] scalingCoefficients;
        double kL = lmsIdealWhite[0][0] / lmsRealWhite[0][0];
        double kM = lmsIdealWhite[0][1] / lmsRealWhite[0][1];
        double kS = lmsIdealWhite[0][2] / lmsRealWhite[0][2];
        scalingCoefficients = new double[][]{{kL, 0.0, 0.0}, {0.0, kM, 0.0}, {0.0, 0.0, kS}};

        pixelData = matrixMultiplicationInstance.multiply(scalingCoefficients, pixelData);

        // zatial bez vratenia z linearizovanej a normalizovanej podoby
        pixelData = matrixMultiplicationInstance.fromLMStoRGB(pixelData);
        pixelData = linearizationInstance.nonLinearize(pixelData);
        pixelData = linearizationInstance.nonNormalize(pixelData);


        return pixelDataInstance.setBitmap(width, height, pixelData);
    }

    public double[][] conversionsToLMS(double[][] pixelData,MatrixMultiplication matrixMultiplicationInstance, Linearization linearizationInstance) {
        pixelData = linearizationInstance.normalize(pixelData);
        pixelData = linearizationInstance.linearize(pixelData);
        return matrixMultiplicationInstance.fromRGBtoLMS(pixelData);
    }

}
