package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld;

import android.graphics.Bitmap;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.Linearization2D;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.MatrixMultiplication2D;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;

/**
 * Created by Vladimira Hezelova on 25. 4. 2015.
 */
public class ConversionGW {
    public Bitmap convert(int width, int height, double[][] pixelData) {

        PixelData pixelDataInstance = new PixelData();
        MatrixMultiplication2D matrixMultiplication2DInstance = new MatrixMultiplication2D();
        Linearization2D linearization2DInstance = new Linearization2D();

        Average average = new Average();
        double[][] scalingMatrix = new double[3][3];

        pixelData = linearization2DInstance.normalize(pixelData);

        scalingMatrix = average.getScalingMatrix(pixelData, scalingMatrix);

        pixelData = matrixMultiplication2DInstance.multiply(scalingMatrix, pixelData);

        pixelData = linearization2DInstance.nonNormalize(pixelData);

        return pixelDataInstance.setBitmap(width, height, pixelData);
    }
}
