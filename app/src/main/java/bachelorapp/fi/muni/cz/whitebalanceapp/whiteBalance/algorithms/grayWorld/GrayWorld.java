package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld;

import android.graphics.Bitmap;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.Convertor;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.Linearization1D;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.MatrixMultiplication1D;

/**
 * Created by Vladimira Hezelova on 25. 4. 2015.
 */
public class GrayWorld extends Convertor {

    private Bitmap originalBitmap;
    private int width;
    private int height;
    private Bitmap convertedBitmap;

    private MatrixMultiplication1D matrixMultiplication1DInstance;
    private Linearization1D linearization1DInstance;

    private float[][] scalingMatrix;

    public GrayWorld(Bitmap image) {
        this.originalBitmap = image;
        this.width = originalBitmap.getWidth();
        this.height = originalBitmap.getHeight();
        this.convertedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        this.matrixMultiplication1DInstance = new MatrixMultiplication1D();
        this.linearization1DInstance = new Linearization1D();
        grayWorld();
    }

    public void grayWorld() {
        int value;
        float[] pixelData = new float[3];

        setScalingMatrix();

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                value = originalBitmap.getPixel(j,i);
                pixelData[0] = (value >> 16) & 0xff; //red;
                pixelData[1] = (value >>  8) & 0xff; //green
                pixelData[2] = (value      ) & 0xff;  //blue

                pixelData = convert(pixelData);
                convertedBitmap.setPixel(j, i, getValueFromRGB(pixelData));
            }
        }
    }

    public Bitmap getConvertedBitmap() {
        return convertedBitmap;
    }

    public void setScalingMatrix() {
        Average average = new Average(originalBitmap);
        scalingMatrix = new float[3][3];
        scalingMatrix = average.getScalingMatrix(scalingMatrix);
    }

    public float[] convert(float[] pixelData) {

        pixelData = linearization1DInstance.normalize(pixelData);
        pixelData = matrixMultiplication1DInstance.multiply(scalingMatrix, pixelData);
        pixelData = linearization1DInstance.nonNormalize(pixelData);

        return pixelData;
    }
}
