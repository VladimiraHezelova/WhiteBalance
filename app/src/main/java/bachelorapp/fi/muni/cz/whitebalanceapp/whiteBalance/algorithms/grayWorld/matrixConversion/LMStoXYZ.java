package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld.matrixConversion;

/**
 * Created by Vladimira Hezelova on 21. 3. 2015.
 */
public class LMStoXYZ {
    // inverse matrix to XYZtoLMS
    // matrix for conversion from LMS to CIE D65 XYZ
    private static final double[][] MATRIX_LMStoXYZ =
            {{1.07645,-0.23766, 0.16121},
             {0.41096, 0.55434, 0.03469},
             {-0.01095,-0.01338,1.02434}};

    public static double[][] fromLMStoXYZ(double[][] lms) {
        int end = lms.length / 4;
        MatrixMultiplication thread0 = new MatrixMultiplication(0, 0, end, MATRIX_LMStoXYZ, lms);
        thread0.start();
        MatrixMultiplication thread1 = new MatrixMultiplication(1, end, end*2, MATRIX_LMStoXYZ, lms);
        thread1.start();
        MatrixMultiplication thread2 = new MatrixMultiplication(2, end*2, end*3, MATRIX_LMStoXYZ, lms);
        thread2.start();
        MatrixMultiplication thread3 = new MatrixMultiplication(3, end*3, lms.length, MATRIX_LMStoXYZ, lms);
        thread3.start();
        while(thread0.isAlive() || thread1.isAlive() || thread2.isAlive() || thread3.isAlive()) {
              System.out.println("vlakna su zive");
        }
        return thread0.getPixelData();
    }
}
