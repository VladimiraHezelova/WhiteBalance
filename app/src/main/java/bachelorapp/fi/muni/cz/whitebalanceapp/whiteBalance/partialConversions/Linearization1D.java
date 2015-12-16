package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions;

/**
 * Created by Vladimira Hezelova on 5. 12. 2015.
 */
public class Linearization1D {

    public float[] linearize(float[] pixelData) {
        if (pixelData[0] > 0.04045f) {
            pixelData[0] = (float) Math.pow(((pixelData[0] + 0.055f) / 1.055f), 2.4f);
        } else {
            pixelData[0] /= 12.92f;
        }
        if (pixelData[1] > 0.04045f) {
            pixelData[1] = (float) Math.pow(((pixelData[1] + 0.055f) / 1.055f), 2.4f);
        } else {
            pixelData[1] /= 12.92f;
        }
        if (pixelData[2] > 0.04045f) {
            pixelData[2] = (float) Math.pow(((pixelData[2] + 0.055f) / 1.055f), 2.4f);
        } else {
            pixelData[2] /= 12.92f;
        }

        return pixelData;
    }

    public float[] normalize(float[] pixelData) {
        pixelData[0] /= 255;
        pixelData[1] /= 255;
        pixelData[2] /= 255;
        return pixelData;
    }

    public float[] nonLinearize(float[] pixelData) {
        if(pixelData[0] * 12.92f <= 0.04045) {
            pixelData[0] = pixelData[0] * 12.92f;
        } else {
            pixelData[0] = (((float) Math.pow(pixelData[0],1/2.4f)) * 1.055f) - 0.055f;
        }

        if(pixelData[1] * 12.92f <= 0.04045) {
            pixelData[1] = pixelData[1] * 12.92f;
        } else {
            pixelData[1] = (((float) Math.pow(pixelData[1],1/2.4f)) * 1.055f) - 0.055f;
        }

        if(pixelData[2] * 12.92f <= 0.04045) {
            pixelData[2] = pixelData[2] * 12.92f;
        } else {
            pixelData[2] = (((float) Math.pow(pixelData[2],1/2.4f)) * 1.055f) - 0.055f;
        }
        return pixelData;
    }

    public float[] nonNormalize(float[] pixelData) {
        pixelData[0] = pixelData[0] * 255;
        pixelData[1] = pixelData[1] * 255;
        pixelData[2] = pixelData[2] * 255;
                //lebo su hodnoty cervenej zaporne
        if(pixelData[0] < 0) {
            pixelData[0] *= -1.0;
        }
        if(pixelData[0] > 255) {
            pixelData[0] = 255;
        }
        if(pixelData[1] < 0) {
            pixelData[1] *= -1.0;
        }
        if(pixelData[1] > 255) {
            pixelData[1] = 255;
        }
        if(pixelData[2] < 0) {
            pixelData[2] *= -1.0;
        }
        if(pixelData[2] > 255) {
            pixelData[2] = 255;
        }

        return pixelData;
    }
}
