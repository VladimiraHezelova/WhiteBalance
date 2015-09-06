package bachelorapp.fi.muni.cz.whitebalanceapp.linearization;

/**
 * Created by Vladimira Hezelova on 26. 4. 2015.
 */
public class LinearizationThread extends Thread {
    private double[][] pixelData;

    public LinearizationThread(int numberOfColorCanal, double[][] pixelData) {
        super(String.valueOf(numberOfColorCanal));
        this.pixelData = pixelData;
    }

    @Override
    public void run() {
        int j = Integer.parseInt(getName());
        for(int i = 0; i < pixelData.length; i++) {
            if (pixelData[i][j] <= 0.04045) {
                pixelData[i][j] = pixelData[i][j] / 12.92;
            } else {
                pixelData[i][j] = Math.pow(((pixelData[i][j] + 0.055) / 1.055), 2.4);
            }
        }
    }

    public double[][] getPixelData() {
        return pixelData;
    }
}
