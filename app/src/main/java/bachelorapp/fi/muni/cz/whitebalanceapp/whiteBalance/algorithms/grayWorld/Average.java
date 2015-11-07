package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld;

/**
 * Created by Vladimira Hezelova on 25. 4. 2015.
 */
public class Average {

    private double avgR;
    private double avgG;
    private double avgB;
    private double avgGray;
    private double kR;
    private double kG;
    private double kB;

    public Average() {
    }

    public double[][] getScalingMatrix(double[][] pixelData, double[][] scalingMatrix) {
        getAverages(pixelData);
        getAvgGray();
        getScalingCoefficients();

        scalingMatrix[0][0] = kR;
        scalingMatrix[1][1] = kG;
        scalingMatrix[2][2] = kB;

        return scalingMatrix;
    }

    private void getAverages(double[][] pixelData) {
        double sumR = 0;
        double sumG = 0;
        double sumB = 0;
        for(int i = 0; i < pixelData.length; i++) {
            for(int j = 0; j < 3; j++) {
                switch(j) {
                    case 0 : sumR += pixelData[i][0];
                        break;
                    case 1 : sumG += pixelData[i][1];
                        break;
                    case 2 : sumB += pixelData[i][2];
                        break;
                }
            }
        }
        double numberOfPixels = pixelData.length;
        avgR = sumR / numberOfPixels;
        avgG = sumG / numberOfPixels;
        avgB = sumB / numberOfPixels;
    }

    private void getAvgGray() {
        avgGray = 0.299 * avgR + 0.587 * avgG + 0.114 * avgB;
    }

    private void getScalingCoefficients() {
        kR = avgGray / avgR;
        kG = avgGray / avgG;
        kB = avgGray / avgB;
    }



}
