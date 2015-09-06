package bachelorapp.fi.muni.cz.whitebalanceapp.linearization;

/**
 * Created by Vladimira Hezelova on 26. 4. 2015.
 */
public class NormalizationThread extends Thread {
    private double[][] pixelData;

    public NormalizationThread(int numberOfColorCanal, double[][] pixelData) {
        super(String.valueOf(numberOfColorCanal));
        this.pixelData = pixelData;
    }

    @Override
    public void run() {
        int j = Integer.parseInt(getName());
        for (int i = 0; i < pixelData.length; i++) {
            pixelData[i][j] = pixelData[i][j] / 255;
        }
    }

    public double[][] getPixelData() {
        return pixelData;
    }
}
