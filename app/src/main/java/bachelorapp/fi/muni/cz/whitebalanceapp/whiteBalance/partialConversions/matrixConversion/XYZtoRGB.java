package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions.matrixConversion;

/**
 * Created by Vladimira Hezelova on 21. 3. 2015.
 */
public class XYZtoRGB {
    // inverse matrix to RGBtoXYZ
    // matrix for conversion from CIE D65 XYZ to RGB
    private static final double[][] MATRIX_XYZtoRGB =
            {{3.24062, -1.53720, -0.49862},
             {-0.96893, 1.87575, 0.04151},
             {0.05571,-0.20402,1.05699}};

    public static double[][] fromXYZtoRGB(double[][] xyz) {
        int end = xyz.length / 4;
        MatrixMultiplication thread0 = new MatrixMultiplication(0, 0, end, MATRIX_XYZtoRGB, xyz);
        thread0.start();
        MatrixMultiplication thread1 = new MatrixMultiplication(1, end, end*2, MATRIX_XYZtoRGB, xyz);
        thread1.start();
        MatrixMultiplication thread2 = new MatrixMultiplication(2, end*2, end*3, MATRIX_XYZtoRGB, xyz);
        thread2.start();
        MatrixMultiplication thread3 = new MatrixMultiplication(3, end*3, xyz.length, MATRIX_XYZtoRGB, xyz);
        thread3.start();
        while(thread0.isAlive() || thread1.isAlive() || thread2.isAlive() || thread3.isAlive()) {
            //  System.out.println("vlakna su zive");
        }
        return thread0.getPixelData();
    }
}
