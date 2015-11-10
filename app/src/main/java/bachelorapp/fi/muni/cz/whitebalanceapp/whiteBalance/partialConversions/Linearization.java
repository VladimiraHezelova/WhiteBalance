package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions;

/**
 * Created by Vladimira Hezelova on 15. 3. 2015.
 */
public class Linearization {

    public double[][] linearize(double[][] pixelData) {
        for(int i = 0; i < pixelData.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (pixelData[i][j] <= 0.04045) {
                    pixelData[i][j] = pixelData[i][j] / 12.92;
                } else {
                    pixelData[i][j] = Math.pow(((pixelData[i][j] + 0.055) / 1.055), 2.4);
                }
            }
        }
        return pixelData;
    }

    public double[][] normalize(double[][] pixelData) {
        for (int i = 0; i < pixelData.length; i++) {
            for (int j = 0; j < 3; j++) {
                pixelData[i][j] = pixelData[i][j] / 255;
            }
        }
        return pixelData;
    }

    public double[][] nonLinearize(double[][] pixelData) {
        double smallRGB;
        double largeRGB;
        for(int i = 0; i < pixelData.length; i++) {
            for (int j = 0; j < 3; j++) {
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
        return pixelData;
    }

    public double[][] nonNormalize(double[][] pixelData) {
        for(int i = 0; i < pixelData.length; i++) {
            for (int j = 0; j < 3; j++) {
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
        return pixelData;
    }
}
