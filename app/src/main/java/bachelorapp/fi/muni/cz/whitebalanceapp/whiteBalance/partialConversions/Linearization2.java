package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions;

/**
 * Created by Vladimira Hezelova on 5. 12. 2015.
 */
public class Linearization2 {

    public double[] linearize(double[] pixelData) {
            for (int j = 0; j < 3; j++) {
                if (pixelData[j] <= 0.04045) {
                    pixelData[j] = pixelData[j] / 12.92;
                } else {
                    pixelData[j] = Math.pow(((pixelData[j] + 0.055) / 1.055), 2.4);
                }
            }

        return pixelData;
    }

    public double[] normalize(double[] pixelData) {
            for (int j = 0; j < 3; j++) {
                pixelData[j] = pixelData[j] / 255;
            }

        return pixelData;
    }

    public double[] nonLinearize(double[] pixelData) {
        double smallRGB;
        double largeRGB;
            for (int j = 0; j < 3; j++) {
                smallRGB = pixelData[j] * 12.92;
                if(smallRGB <= 0.04045) {
                    pixelData[j] = smallRGB;
                } else {
                    largeRGB = Math.pow(pixelData[j],1/2.4);
                    largeRGB *= 1.055;
                    largeRGB -= 0.055;
                    pixelData[j] = largeRGB;
                }
            }

        return pixelData;
    }

    public double[] nonNormalize(double[] pixelData) {
            for (int j = 0; j < 3; j++) {
                pixelData[j] = pixelData[j] * 255;
                //lebo su hodnoty cervenej zaporne
                if(pixelData[j] < 0) {
                    pixelData[j] *= -1.0;
                }
                if(pixelData[j] > 255) {
                    pixelData[j] = 255;
                }
            }

        return pixelData;
    }
}
