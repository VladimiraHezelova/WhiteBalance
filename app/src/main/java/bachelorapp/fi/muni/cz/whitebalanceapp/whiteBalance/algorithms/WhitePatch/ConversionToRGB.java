package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.matrixConversion.LMStoXYZ;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.matrixConversion.XYZtoRGB;

/**
 * Created by Vladimira Hezelova on 21. 3. 2015.
 */
public class ConversionToRGB {

    /**
     * Prevedie pole hodnot pixelov v LMS zo vstupneho parametru na hodnoty v RGB a tie vrati
     *
     * @param lms pole hodnot pixelov v LMS
     * @return rgb pole hodnot pixelov v RGB
     */
    public static double[][] convert(double[][] lms) {
        lms = LMStoXYZ.fromLMStoXYZ(lms);
        lms = XYZtoRGB.fromXYZtoRGB(lms);
        return lms;
    }
}
