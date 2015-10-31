package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld.matrixConversion;

/**
 * Created by Vladimira Hezelova on 16. 3. 2015.
 */
public class RGBtoXYZ {
    // matrix for conversion from normalized, linearized RGB to CIE D65 XYZ
    private static final double[][] MATRIX_RGBtoXYZ =
            {{0.4124,0.3576, 0.1805},
             {0.2126, 0.7152, 0.0722},
             {0.0193,0.1192,0.9505}};

    public static double[][] fromRGBtoXYZ(double[][] rgb) {
        int end = rgb.length / 4;
        MatrixMultiplication thread0 = new MatrixMultiplication(0, 0, end, MATRIX_RGBtoXYZ, rgb);
        thread0.start();
        MatrixMultiplication thread1 = new MatrixMultiplication(1, end, end*2, MATRIX_RGBtoXYZ, rgb);
        thread1.start();
        MatrixMultiplication thread2 = new MatrixMultiplication(2, end*2, end*3, MATRIX_RGBtoXYZ, rgb);
        thread2.start();
        MatrixMultiplication thread3 = new MatrixMultiplication(3, end*3, rgb.length, MATRIX_RGBtoXYZ, rgb);
        thread3.start();
        while(thread0.isAlive() || thread1.isAlive() || thread2.isAlive() || thread3.isAlive()) {
              System.out.println("vlakna su zive");
        }
        return thread0.getPixelData();
    }

}
