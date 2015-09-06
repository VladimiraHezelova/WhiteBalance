package bachelorapp.fi.muni.cz.whitebalanceapp.linearization;

/**
 * Created by Vladimira Hezelova on 26. 4. 2015.
 */
public class NonNormalizationThread extends Thread {
    private double[][] pixelData;
    public NonNormalizationThread(int numberOfColorCanal, double[][] pixelData) {
        super(String.valueOf(numberOfColorCanal));
        this.pixelData = pixelData;
    }

    @Override
    public void run() {
        int j = Integer.parseInt(getName());
        for(int i = 0; i < pixelData.length; i++) {
            pixelData[i][j] = pixelData[i][j] * 255;
            //lebo su hodnoty cervenej zaporne
            if(pixelData[i][j] < 0) {
                pixelData[i][j] *= -1.0;
            }
            if(pixelData[i][j] > 255) {
                pixelData[i][j] = 255;
            }
        }
    }

    public double[][] getPixelData() {
        return pixelData;
    }
}
