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
        LinearizationThread thread0 = new LinearizationThread(0, pixelData);
        thread0.start();
        LinearizationThread thread1 = new LinearizationThread(1, pixelData);
        thread1.start();
        LinearizationThread thread2 = new LinearizationThread(2, pixelData);
        thread2.start();
        while(thread0.isAlive() || thread1.isAlive() || thread2.isAlive()) {
            //  System.out.println("vlakna su zive");
        }
        pixelData = thread0.getPixelData();
        return pixelData;
    }

    /**
     * Normalizuje hondoty pixelov z[0-255] na rozsah [0-1]
     */
    public static double[][] normalization(double[][] pixelData) {
        NormalizationThread thread0 = new NormalizationThread(0, pixelData);
        thread0.start();
        NormalizationThread thread1 = new NormalizationThread(1, pixelData);
        thread1.start();
        NormalizationThread thread2 = new NormalizationThread(2, pixelData);
        thread2.start();
        while(thread0.isAlive() || thread1.isAlive() || thread2.isAlive()) {
            //  System.out.println("vlakna su zive");
        }
        pixelData = thread0.getPixelData();
        // hodnoty
        /*
        for(int i = 0; i < 100; i++) {
            Log.e("normalization", Double.toString(pixelData[i][0]));
            Log.e("normalization", Double.toString(pixelData[i][1]));
            Log.e("normalization", Double.toString(pixelData[i][2]));
        }
        */
        return pixelData;
    }
}
