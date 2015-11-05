package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.linearization;

/**
 * Created by Vladimira Hezelova on 15. 3. 2015.
 */
public class Linearization {

    /**
     * Normalizuje a nasledne linearizuje hodnoty pixelov v poli
     *
     * @return linearizovane pole
     */
    public static double[][] linearization(double[][] pixelData) {


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

    /**
     * Normalizuje hondoty pixelov z[0-255] na rozsah [0-1]
     */
    public static double[][] normalization(double[][] pixelData) {
        for (int i = 0; i < pixelData.length; i++) {
            for (int j = 0; j < 3; j++) {
                // Log.e("pred", Double.toString(pixelData[i][j]));
                pixelData[i][j] = pixelData[i][j] / 255;
                //Log.e("po", Double.toString(pixelData[i][j]));
            }

        }
        return pixelData;
    }
}
