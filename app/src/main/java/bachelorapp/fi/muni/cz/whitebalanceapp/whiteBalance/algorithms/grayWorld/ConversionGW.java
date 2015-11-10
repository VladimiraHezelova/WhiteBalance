package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld;

import android.graphics.Bitmap;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.Linearization;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.MatrixMultiplication;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;

/**
 * Created by Vladimira Hezelova on 25. 4. 2015.
 */
public class ConversionGW {
    public Bitmap convert(int width, int height, double[][] pixelData) {

        PixelData pixelDataInstance = new PixelData();
        MatrixMultiplication matrixMultiplicationInstance = new MatrixMultiplication();
        Linearization linearizationInstance = new Linearization();

        Average average = new Average();
        double[][] scalingMatrix = new double[3][3];

        pixelData = linearizationInstance.normalize(pixelData);

        scalingMatrix = average.getScalingMatrix(pixelData, scalingMatrix);

        pixelData = matrixMultiplicationInstance.multiply(scalingMatrix, pixelData);

        pixelData = linearizationInstance.nonNormalize(pixelData);

        return pixelDataInstance.setBitmap(width, height, pixelData);
    }
}
