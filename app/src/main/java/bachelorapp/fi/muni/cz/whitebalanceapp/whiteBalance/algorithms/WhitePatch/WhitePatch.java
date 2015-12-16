package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch;


import android.graphics.Bitmap;
import android.util.Log;

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
    private MatrixMultiplication1D mm1;
    private MatrixMultiplication1D mm2;
    private Linearization1D linearization1DInstance;
    private float[] array2;

    private float[][] scalingMatrix;
    public static int counter;
    private float uvwCopy[];
    private float uvwCopy2[];
    float[] array;

    /*
    private float u;
    private float v;
    private float w;
    */
    /*
    private float x;
    private float y;
    private float z;
    */

    public WhitePatch(Bitmap image, Bitmap selectedWhite) {
        super(image);
        this.selectedWhite = selectedWhite;

        this.matrixMultiplication1DInstance = new MatrixMultiplication1D();
        this.linearization1DInstance = new Linearization1D();
        array2 = new float[3];
        counter = 0;
        uvwCopy = new float[3];
        uvwCopy2 = new float[3];
        array = new float[3];
        setScalingMatrix();
        balanceWhite();
    }

    public void setScalingMatrix() {
        long start = System.currentTimeMillis();

        int median = median(selectedWhite);

        long end = System.currentTimeMillis();
        double time = (double) (end - start) / 1000;
        Log.i("balanceWhite", "median : " + time + "seconds");

        float[] rgbRealWhite = new float[3];
        rgbRealWhite = getRGBFromValue(median, rgbRealWhite);

        float[] lmsRealWhite = conversionsToXYZ(rgbRealWhite);
        float[] outRGBreal = new float[3];
        lmsRealWhite = conversionsToLMS(lmsRealWhite, outRGBreal);
        float[] lmsIdealWhite = conversionsToXYZ(new float[]{255, 255, 255});
        float[] outRGBideal = new float[3];
        lmsIdealWhite = conversionsToLMS(lmsIdealWhite, outRGBideal);


        float kL = lmsIdealWhite[0] / lmsRealWhite[0];
        float kM = lmsIdealWhite[1] / lmsRealWhite[1];
        float kS = lmsIdealWhite[2] / lmsRealWhite[2];
        /*
        float kL = lmsIdealWhite[0] / 200f;
        float kM = lmsIdealWhite[1] / 260f;
        float kS = lmsIdealWhite[2] / 170f;

*/

        scalingMatrix = new float[][]{{kL, 0.0f, 0.0f}, {0.0f, kM, 0.0f}, {0.0f, 0.0f, kS}};
    }


    @Override
    public float[] removeColorCast(float[] pixelData, float[] outRGB){
        pixelData = conversionsToXYZ(pixelData); // 5.11
        pixelData = conversionsToLMS(pixelData, outRGB);
        pixelData = matrixMultiplication1DInstance.multiply(scalingMatrix, pixelData); //5.25
        pixelData = conversionsToXYZ2(pixelData);
        pixelData = conversionToRGB(pixelData, outRGB); //5.37
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
        /*
        array[0] = MatrixMultiplication1D.MATRIX_XYZtoLMS[0][0] * pixelData[0] + MatrixMultiplication1D.MATRIX_XYZtoLMS[0][1] * pixelData[1] + MatrixMultiplication1D.MATRIX_XYZtoLMS[0][2] * pixelData[2];
        array[1] = MatrixMultiplication1D.MATRIX_XYZtoLMS[1][0] * pixelData[0] + MatrixMultiplication1D.MATRIX_XYZtoLMS[1][1] * pixelData[1] + MatrixMultiplication1D.MATRIX_XYZtoLMS[1][2] * pixelData[2];
        array[2] = MatrixMultiplication1D.MATRIX_XYZtoLMS[2][0] * pixelData[0] + MatrixMultiplication1D.MATRIX_XYZtoLMS[2][1] * pixelData[1] + MatrixMultiplication1D.MATRIX_XYZtoLMS[2][2] * pixelData[2];
*/

/*
        float a = MatrixMultiplication1D.MATRIX_XYZtoLMS[0][0] * pixelData[0] + MatrixMultiplication1D.MATRIX_XYZtoLMS[0][1] * pixelData[1] + MatrixMultiplication1D.MATRIX_XYZtoLMS[0][2] * pixelData[2];
        float b = MatrixMultiplication1D.MATRIX_XYZtoLMS[1][0] * pixelData[0] + MatrixMultiplication1D.MATRIX_XYZtoLMS[1][1] * pixelData[1] + MatrixMultiplication1D.MATRIX_XYZtoLMS[1][2] * pixelData[2];
        float c = MatrixMultiplication1D.MATRIX_XYZtoLMS[2][0] * pixelData[0] + MatrixMultiplication1D.MATRIX_XYZtoLMS[2][1] * pixelData[1] + MatrixMultiplication1D.MATRIX_XYZtoLMS[2][2] * pixelData[2];
        pixelData[0] = a;
        pixelData[1] = b;
        pixelData[2] = c;
        */
        /*
        float array[] = new float[3];
        array[0] = MatrixMultiplication1D.MATRIX_XYZtoLMS[0][0] * pixelData[0] + MatrixMultiplication1D.MATRIX_XYZtoLMS[0][1] * pixelData[1] + MatrixMultiplication1D.MATRIX_XYZtoLMS[0][2] * pixelData[2];
        array[1] = MatrixMultiplication1D.MATRIX_XYZtoLMS[1][0] * pixelData[0] + MatrixMultiplication1D.MATRIX_XYZtoLMS[1][1] * pixelData[1] + MatrixMultiplication1D.MATRIX_XYZtoLMS[1][2] * pixelData[2];
        array[2] = MatrixMultiplication1D.MATRIX_XYZtoLMS[2][0] * pixelData[0] + MatrixMultiplication1D.MATRIX_XYZtoLMS[2][1] * pixelData[1] + MatrixMultiplication1D.MATRIX_XYZtoLMS[2][2] * pixelData[2];
*/
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
