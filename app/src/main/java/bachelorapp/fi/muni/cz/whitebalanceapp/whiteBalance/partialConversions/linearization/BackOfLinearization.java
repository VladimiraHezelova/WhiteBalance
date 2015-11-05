package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.linearization;

/**
 * Created by Vladimira Hezelova on 21. 3. 2015.
 *
 * Pocita hodnoty pixelov po korekcii a prekonvertovane spat v RGB
 * inverznymi vzorcami, ktore ich na zaciatku linearizovali a normalizovali
 */
public class BackOfLinearization {
    /**
     * Prepocita hodnoty pixelov inverznym vzorcom k linearizacii a na konci vola metodu na nenormalizaciu
     *
     * @return nelinearizovane a nenormalizovane pole RGB hodnot
     */


    public static double[][] backOfLinearization(double[][] pixelData) {

        double smallRGB;
        double largeRGB;
        for(int i = 0; i < pixelData.length; i++) {
            //   System.out.println(j + ". vlakno " + pixelData[i][j]);
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

    /**
     * Roztiahne rozsah hodonot pixelov na [0-255] vynasobenim hodnotou 255,
     * kedze kvoli normalizacii boli hodnotou 255 vydelene
     *
     * @return nenoramalizovane pole RGB hodnot
     */
    public static double[][] backOfNormalization(double[][] pixelData) {
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
