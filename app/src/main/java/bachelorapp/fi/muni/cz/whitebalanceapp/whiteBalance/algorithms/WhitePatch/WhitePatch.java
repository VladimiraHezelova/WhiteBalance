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

    private String imagePath;
    private Bitmap selectedWhite;

    private Bitmap originalBitmap;
    private int width;
    private int height;
    private Bitmap convertedBitmap;

    private MatrixMultiplication1D matrixMultiplication1DInstance;
    private Linearization1D linearization1DInstance;

    private float[][] scalingMatrix;

    public WhitePatch(String imagePath, Bitmap image, Bitmap selectedWhite) {

        this.imagePath = imagePath;
        this.selectedWhite = selectedWhite;

        this.originalBitmap = image;
        this.width = originalBitmap.getWidth();
        this.height = originalBitmap.getHeight();
        this.convertedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        this.matrixMultiplication1DInstance = new MatrixMultiplication1D();
        this.linearization1DInstance = new Linearization1D();
        whitePatch();
    }

    public void whitePatch() {
        int value;
        float[] rgb = new float[3];

        setScalingMatrix();

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                value = originalBitmap.getPixel(j,i);
                rgb = getRGBFromValue(value, rgb);
                rgb = convert(rgb);
                convertedBitmap.setPixel(j, i, getValueFromRGB(rgb));
            }
        }
    }

    public int median(Bitmap img) {
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

    public Bitmap getConvertedBitmap() {
        return convertedBitmap;
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

    public float[] convert(float[] pixelData){
        pixelData = conversionsToLMS(pixelData);
        pixelData = matrixMultiplication1DInstance.multiply(scalingMatrix, pixelData);
        pixelData = conversionToRGB(pixelData);
        return pixelData;
    }

    public float[] conversionsToLMS(float[] pixelData) {
        pixelData = linearization1DInstance.normalize(pixelData);
        pixelData = linearization1DInstance.linearize(pixelData);
        pixelData = matrixMultiplication1DInstance.fromRGBtoLMS(pixelData);
        return pixelData;
    }
    
    public float[] conversionToRGB(float[] pixelData) {
        pixelData = matrixMultiplication1DInstance.fromLMStoRGB(pixelData);
        pixelData = linearization1DInstance.nonLinearize(pixelData);
        pixelData = linearization1DInstance.nonNormalize(pixelData);
        return pixelData;
    }

    public void recycleBitmpas() {
        convertedBitmap.recycle();
    }

}
