package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld.matrixConversion;

/**
 * Created by Vladimira Hezelova on 16. 3. 2015.
 */
public class XYZtoLMS {
    // matrix for conversion from CIE D65 XYZ to LMS (tristimulus values of the Long, Medium, Short)
    private static final double[][] MATRIX_XYZtoLMS =
            {{0.7982,0.3389, -0.1371},
             {-0.5918, 1.5512, 0.0406},
             {0.0008,0.0239,0.9753}};

    public static double[][] fromXYZtoLMS(double[][] xyz) {
        int end = xyz.length / 4;
        MatrixMultiplication thread0 = new MatrixMultiplication(0, 0, end, MATRIX_XYZtoLMS, xyz);
        thread0.start();
        MatrixMultiplication thread1 = new MatrixMultiplication(1, end, end*2, MATRIX_XYZtoLMS, xyz);
        thread1.start();
        MatrixMultiplication thread2 = new MatrixMultiplication(2, end*2, end*3, MATRIX_XYZtoLMS, xyz);
        thread2.start();
        MatrixMultiplication thread3 = new MatrixMultiplication(3, end*3, xyz.length, MATRIX_XYZtoLMS, xyz);
        thread3.start();
        while(thread0.isAlive() || thread1.isAlive() || thread2.isAlive() || thread3.isAlive()) {
              System.out.println("vlakna su zive");
        }
        return thread0.getPixelData();
    }
}
