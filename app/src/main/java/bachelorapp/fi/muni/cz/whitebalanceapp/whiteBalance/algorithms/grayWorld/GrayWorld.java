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

    private MatrixMultiplication1D matrixMultiplication1DInstance;
    private Linearization1D linearization1DInstance;

    private float[][] scalingMatrix;

    public GrayWorld(Bitmap image) {
        super(image);
        this.originalBitmap = image;

        this.matrixMultiplication1DInstance = new MatrixMultiplication1D();
        this.linearization1DInstance = new Linearization1D();
        setScalingMatrix();
        balanceWhite();
    }

    public void setScalingMatrix() {
        Average average = new Average(originalBitmap);
        scalingMatrix = average.getScalingMatrix();
    }

    @Override
    public float[] removeColorCast(float[] pixelData, float[] outRGB) {
        pixelData = linearization1DInstance.normalize(pixelData);
        pixelData = matrixMultiplication1DInstance.multiply(scalingMatrix, pixelData);
        pixelData = linearization1DInstance.nonNormalize(pixelData);
        return pixelData;
    }
}
