package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld.linearization;

/**
 * Created by Vladimira Hezelova on 26. 4. 2015.
 */
public class NonLinearizationThread extends Thread {
    private double[][] pixelData;
    public NonLinearizationThread(int numberOfColorCanal, double[][] pixelData) {
        super(String.valueOf(numberOfColorCanal));
        this.pixelData = pixelData;
    }

    @Override
    public void run() {
        double smallRGB;
        double largeRGB;
        int j = Integer.parseInt(getName());
        for(int i = 0; i < pixelData.length; i++) {
         //   System.out.println(j + ". vlakno " + pixelData[i][j]);
            smallRGB = pixelData[i][j] * 12.92;
            if(smallRGB <= 0.04045) {
                pixelData[i][j] = smallRGB;
            } else {
                largeRGB = Math.pow(pixelData[i][j],1/2.4);
                largeRGB *= 1.055;
                largeRGB -= 0.055;
                pixelData[i][j] = largeRGB;
            }
        }
    }

    public double[][] getPixelData() {
        return pixelData;
    }
}
