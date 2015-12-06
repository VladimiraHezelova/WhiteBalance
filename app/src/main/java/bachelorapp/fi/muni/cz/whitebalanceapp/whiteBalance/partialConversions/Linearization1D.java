package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions;

/**
 * Created by Vladimira Hezelova on 5. 12. 2015.
 */
public class Linearization1D {

    public float[] linearize(float[] pixelData) {
        for (int j = 0; j < 3; j++) {
            if (pixelData[j] <= 0.04045f) {
                pixelData[j] = pixelData[j] / 12.92f;
            } else {
                pixelData[j] = (float) Math.pow(((pixelData[j] + 0.055f) / 1.055f), 2.4f);
            }
        }
        return pixelData;
    }

    public float[] normalize(float[] pixelData) {
        for (int j = 0; j < 3; j++) {
            pixelData[j] = pixelData[j] / 255;
        }
        return pixelData;
    }

    public float[] nonLinearize(float[] pixelData) {
        float smallRGB;
        float largeRGB;
            for (int j = 0; j < 3; j++) {
                smallRGB = pixelData[j] * 12.92f;
                if(smallRGB <= 0.04045) {
                    pixelData[j] = smallRGB;
                } else {
                    largeRGB = (float) Math.pow(pixelData[j],1/2.4f);
                    largeRGB *= 1.055f;
                    largeRGB -= 0.055f;
                    pixelData[j] = largeRGB;
                }
            }
        return pixelData;
    }

    public float[] nonNormalize(float[] pixelData) {
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
