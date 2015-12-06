package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.Linearization;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.Linearization2;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.MatrixMultiplication;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.MatrixMultiplication2;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;


/**
 * Created by Vladimira Hezelova on 15. 3. 2015.
 * <p/>
 * Image Chromatic Adapatation - White Patch (WP) Method
 */
public class Conversion {

    private String imagePath;
    private Bitmap selectedWhite;

    private Bitmap originalBitmap;
    private Bitmap convertedBitmap;

    MatrixMultiplication2 matrixMultiplicationInstance;
    Linearization2 linearizationInstance;

    double[][] scalingCoefficients;

    public Conversion(String imagePath, Bitmap selectedWhite) {
        this.imagePath = imagePath;
        this.selectedWhite = selectedWhite;
        matrixMultiplicationInstance = new MatrixMultiplication2();
        linearizationInstance = new Linearization2();
        decodePhoto();
    }

    public void decodePhoto() {
        originalBitmap = BitmapFactory.decodeFile(imagePath);
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        convertedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        PixelData pixelDataInstance = new PixelData();

        int value;
        double[] pixelData = new double[3];
        setScalingMatrix();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                value = originalBitmap.getPixel(j,i);
                pixelData[0] = (value >> 16) & 0xff; //red;
                pixelData[1] = (value >>  8) & 0xff; //green
                pixelData[2] = (value      ) & 0xff;  //blue


                pixelData = convert(selectedWhite, pixelData);
                convertedBitmap.setPixel(j, i, getValue(pixelData));
            }
        }
    }

    public int getValue(double[] pixelData) {
        int R = (int) pixelData[0];
        int G = (int) pixelData[1];
        int B = (int) pixelData[2];
        return ((R & 0xFF) << 16) | ((G & 0xFF) << 8)  | ((B & 0xFF));
    }

    public Bitmap getConvertedBitmap() {
        return convertedBitmap;
    }

    public void setScalingMatrix() {
        PixelData pixelDataInstance = new PixelData();
        MatrixMultiplication matrixMultiplicationInstance2 = new MatrixMultiplication();
        Linearization linearizationInstance2 = new Linearization();
        // konverzia povodneho obrazu do LMS



        // konverzia "realnej" bielej do LMS
        //  double[][] lmsRealWhite = conversionsToLMS(PixelData.getPixelDataForRealWhite(realWhiteImg));
        double[] rgb = new double[3];
        double[][] pixelDataForRealWhite = new double[][]{pixelDataInstance.getPixelDataFromValue(pixelDataInstance.median(selectedWhite), rgb)};
        double[][] lmsRealWhite = conversionsToLMS2(pixelDataForRealWhite, matrixMultiplicationInstance2, linearizationInstance2);



        // konverzia "idealnej" bielej do LMS
        double[][] lmsIdealWhite = conversionsToLMS2(new double[][]{{255, 255, 255}}, matrixMultiplicationInstance2, linearizationInstance2);


        double kL = lmsIdealWhite[0][0] / lmsRealWhite[0][0];
        double kM = lmsIdealWhite[0][1] / lmsRealWhite[0][1];
        double kS = lmsIdealWhite[0][2] / lmsRealWhite[0][2];
        scalingCoefficients = new double[][]{{kL, 0.0, 0.0}, {0.0, kM, 0.0}, {0.0, 0.0, kS}};
    }


    public double[] convert(Bitmap realWhiteImg, double[] pixelData){


        pixelData = conversionsToLMS(pixelData, matrixMultiplicationInstance, linearizationInstance);

        pixelData = matrixMultiplicationInstance.multiply(scalingCoefficients, pixelData);

        // zatial bez vratenia z linearizovanej a normalizovanej podoby
        pixelData = matrixMultiplicationInstance.fromLMStoRGB(pixelData);



        pixelData = linearizationInstance.nonLinearize(pixelData);
        pixelData = linearizationInstance.nonNormalize(pixelData);


        return pixelData;
    }

    public double[] conversionsToLMS(double[] pixelData,MatrixMultiplication2 matrixMultiplicationInstance, Linearization2 linearizationInstance) {
        pixelData = linearizationInstance.normalize(pixelData);
        pixelData = linearizationInstance.linearize(pixelData);
        return matrixMultiplicationInstance.fromRGBtoLMS(pixelData);
    }

    public double[][] conversionsToLMS2(double[][] pixelData,MatrixMultiplication matrixMultiplicationInstance, Linearization linearizationInstance) {
        pixelData = linearizationInstance.normalize(pixelData);
        pixelData = linearizationInstance.linearize(pixelData);
        return matrixMultiplicationInstance.fromRGBtoLMS(pixelData);
    }

}
