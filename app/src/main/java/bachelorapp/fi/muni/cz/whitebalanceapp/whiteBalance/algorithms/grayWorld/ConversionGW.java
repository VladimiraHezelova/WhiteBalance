package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld;

import android.graphics.Bitmap;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.Linearization;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.MatrixMultiplication;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.PixelData;

/**
 * Created by Vladimira Hezelova on 25. 4. 2015.
 */
public class ConversionGW {
    public static Bitmap convert(Bitmap bitmap, double[][] pixelData) {

        Average average = new Average();
        double[][] scalingMatrix = new double[3][3];

        pixelData = Linearization.normalize(pixelData);

        scalingMatrix = average.getScalingMatrix(pixelData, scalingMatrix);

        pixelData = MatrixMultiplication.multiply(scalingMatrix, pixelData);

        pixelData = Linearization.nonNormalize(pixelData);

        return PixelData.setBitmap(bitmap.getWidth(), bitmap.getHeight(), pixelData);
    }
}
