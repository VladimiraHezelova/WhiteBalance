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

        float[] rgbRealWhite = getRGBFromValue(median, new float[3]);
        float[] lmsRealWhite = conversionsToXYZ(rgbRealWhite);
        lmsRealWhite = conversionsToLMS(lmsRealWhite, new float[3]);

        float[] lmsIdealWhite = conversionsToXYZ(new float[]{255, 255, 255});
        lmsIdealWhite = conversionsToLMS(lmsIdealWhite, new float[3]);

        float kL = lmsIdealWhite[0] / lmsRealWhite[0];
        float kM = lmsIdealWhite[1] / lmsRealWhite[1];
        float kS = lmsIdealWhite[2] / lmsRealWhite[2];

        scalingMatrix = new float[][]{{kL, 0.0f, 0.0f}, {0.0f, kM, 0.0f}, {0.0f, 0.0f, kS}};
    }


    @Override
    public float[] removeColorCast(float[] pixelData, float[] outRGB){
        pixelData = conversionsToXYZ(pixelData);
        pixelData = conversionsToLMS(pixelData, outRGB);
        pixelData = matrixMultiplication1DInstance.multiply(scalingMatrix, pixelData);
        pixelData = conversionsToXYZ2(pixelData);
        pixelData = conversionToRGB(pixelData, outRGB);
        return pixelData;
    }

    private float[] conversionsToXYZ(float[] pixelData) {
        pixelData = linearization1DInstance.normalize(pixelData);
        pixelData = linearization1DInstance.linearize(pixelData);
        pixelData = matrixMultiplication1DInstance.multiply(MatrixMultiplication1D.MATRIX_RGBtoXYZ, pixelData);
        return pixelData;
    }
    private float[] conversionsToLMS(float[] pixelData, float[] outRGB) {
        pixelData = matrixMultiplication1DInstance.multiply(MatrixMultiplication1D.MATRIX_XYZtoLMS, pixelData, outRGB);
        return pixelData;
    }

    private float[] conversionsToXYZ2(float[] pixelData) {
        pixelData = matrixMultiplication1DInstance.multiply(MatrixMultiplication1D.MATRIX_LMStoXYZ, pixelData);
        return pixelData;
    }

    private float[] conversionToRGB(float[] pixelData, float[] outRGB) {
        pixelData = matrixMultiplication1DInstance.multiply(MatrixMultiplication1D.MATRIX_XYZtoRGB, pixelData, outRGB);
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
                if(img != null && !img.isRecycled()) {
                    m[i * width + j] = img.getPixel(j, i);
                }
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
