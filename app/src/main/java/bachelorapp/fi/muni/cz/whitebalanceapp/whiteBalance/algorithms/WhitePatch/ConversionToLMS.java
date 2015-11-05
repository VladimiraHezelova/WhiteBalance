package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.whitePatch;

import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.linearization.Linearization;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.matrixConversion.RGBtoXYZ;
import bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.matrixConversion.XYZtoLMS;

/**
 * Created by Vladimira Hezelova on 20. 3. 2015.
 * <p/>
 * Trieda na konverziu z RGB do LMS
 */
public class ConversionToLMS {

    public static double[][] lms;

    /**
     * Trieda postupne vytvara instancie dalsich tried,na ktorych pomocou metod
     * prevadza hondoty pixelov a vklada ich do dvojrozmernych poli
     *
     * @param pixelData hodnoty pixelov v obrazku, prvy rozmer urcuje poradie pixelu v obraze,
     *                  druhy zlozku rgb ([0]=red,[1]=green,[2]=blue)
     * @return
     */
    public static double[][] convert(double[][] pixelData) {

        pixelData = Linearization.normalization(pixelData);
        pixelData = Linearization.linearization(pixelData);
        pixelData = RGBtoXYZ.fromRGBtoXYZ(pixelData);
        pixelData = XYZtoLMS.fromXYZtoLMS(pixelData);

        return pixelData;
    }


}
