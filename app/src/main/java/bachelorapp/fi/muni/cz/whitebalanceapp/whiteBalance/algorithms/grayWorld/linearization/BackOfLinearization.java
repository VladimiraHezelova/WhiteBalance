package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld.linearization;

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

        long start = System.currentTimeMillis();

        NonLinearizationThread thread0 = new NonLinearizationThread(0, pixelData);
        thread0.start();
        NonLinearizationThread thread1 = new NonLinearizationThread(1, pixelData);
        thread1.start();
        NonLinearizationThread thread2 = new NonLinearizationThread(2, pixelData);
        thread2.start();
        while(thread0.isAlive() || thread1.isAlive() || thread2.isAlive()) {
            System.out.println("vlakna su zive");
        }
        pixelData = thread0.getPixelData();
        long end = System.currentTimeMillis();
        double time = (double) (end - start) / 1000;
        System.out.println("backOfLinearization " + time + "sekund");
        return pixelData;
    }

    /**
     * Roztiahne rozsah hodonot pixelov na [0-255] vynasobenim hodnotou 255,
     * kedze kvoli normalizacii boli hodnotou 255 vydelene
     *
     * @return nenoramalizovane pole RGB hodnot
     */
    public static double[][] backOfNormalization(double[][] pixelData) {
        long start = System.currentTimeMillis();

        NonNormalizationThread thread0 = new NonNormalizationThread(0, pixelData);
        thread0.start();
        NonNormalizationThread thread1 = new NonNormalizationThread(1, pixelData);
        thread1.start();
        NonNormalizationThread thread2 = new NonNormalizationThread(2, pixelData);
        thread2.start();
        while(thread0.isAlive() || thread1.isAlive() || thread2.isAlive()) {
            System.out.println("vlakna su zive");
        }
        pixelData = thread0.getPixelData();

        long end = System.currentTimeMillis();
        double time = (double) (end - start) / 1000;
        System.out.println("backOfNormalization " + time + "sekund");
        return pixelData;
    }
}
