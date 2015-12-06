package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch;


import android.graphics.Bitmap;

import java.util.Arrays;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.Convertor;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.Linearization1D;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.MatrixMultiplication1D;


/**
 * Created by Vladimira Hezelova on 15. 3. 2015.
 * <p/>
 * Image Chromatic Adapatation - White Patch (WP) Method
 */
public class WhitePatch extends Convertor {

    private Bitmap selectedWhite;


    private MatrixMultiplication1D matrixMultiplication1DInstance;
    private Linearization1D linearization1DInstance;

    private float[][] scalingMatrix;

    public WhitePatch(Bitmap image, Bitmap selectedWhite) {
        super(image);
        this.selectedWhite = selectedWhite;

        this.matrixMultiplication1DInstance = new MatrixMultiplication1D();
        this.linearization1DInstance = new Linearization1D();
        setScalingMatrix();
        balanceWhite();
    }

    public void setScalingMatrix() {
        int median = median(selectedWhite);
        float[] rgbRealWhite = new float[3];
        rgbRealWhite = getRGBFromValue(median, rgbRealWhite);

        float[] lmsRealWhite = conversionsToLMS(rgbRealWhite);
        float[] lmsIdealWhite = conversionsToLMS(new float[]{255, 255, 255});

        float kL = lmsIdealWhite[0] / lmsRealWhite[0];
        float kM = lmsIdealWhite[1] / lmsRealWhite[1];
        float kS = lmsIdealWhite[2] / lmsRealWhite[2];
        scalingMatrix = new float[][]{{kL, 0.0f, 0.0f}, {0.0f, kM, 0.0f}, {0.0f, 0.0f, kS}};
    }


    @Override
    public float[] removeColorCast(float[] pixelData){
        pixelData = conversionsToLMS(pixelData);
        pixelData = matrixMultiplication1DInstance.multiply(scalingMatrix, pixelData);
        pixelData = conversionToRGB(pixelData);
        return pixelData;
    }

    private float[] conversionsToLMS(float[] pixelData) {
        pixelData = linearization1DInstance.normalize(pixelData);
        pixelData = linearization1DInstance.linearize(pixelData);
        pixelData = matrixMultiplication1DInstance.fromRGBtoLMS(pixelData);
        return pixelData;
    }

    private float[] conversionToRGB(float[] pixelData) {
        pixelData = matrixMultiplication1DInstance.fromLMStoRGB(pixelData);
        pixelData = linearization1DInstance.nonLinearize(pixelData);
        pixelData = linearization1DInstance.nonNormalize(pixelData);
        return pixelData;
    }

    private int median(Bitmap img) {
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
}
