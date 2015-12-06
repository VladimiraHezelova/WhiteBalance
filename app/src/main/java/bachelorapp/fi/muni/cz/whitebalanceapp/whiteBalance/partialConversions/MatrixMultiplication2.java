package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions;

/**
 * Created by Vladimira Hezelova on 5. 12. 2015.
 */
public class MatrixMultiplication2 {
    // matrix for conversion from normalized, linearized RGB to CIE D65 XYZ
    private static final double[][] MATRIX_RGBtoXYZ =
            {{0.4124,0.3576, 0.1805},
                    {0.2126, 0.7152, 0.0722},
                    {0.0193,0.1192,0.9505}};

    // matrix for conversion from CIE D65 XYZ to LMS (tristimulus values of the Long, Medium, Short)
    private static final double[][] MATRIX_XYZtoLMS =
            {{0.7982,0.3389, -0.1371},
                    {-0.5918, 1.5512, 0.0406},
                    {0.0008,0.0239,0.9753}};

    // inverse matrix to XYZtoLMS
    // matrix for conversion from LMS to CIE D65 XYZ
    private static final double[][] MATRIX_LMStoXYZ =
            {{1.07645,-0.23766, 0.16121},
                    {0.41096, 0.55434, 0.03469},
                    {-0.01095,-0.01338,1.02434}};

    // inverse matrix to RGBtoXYZ
    // matrix for conversion from CIE D65 XYZ to RGB
    private static final double[][] MATRIX_XYZtoRGB =
            {{3.24062, -1.53720, -0.49862},
                    {-0.96893, 1.87575, 0.04151},
                    {0.05571,-0.20402,1.05699}};



    /**
     * |x| |a b c||u|
     * |y|=|d e f||v|
     * |z| |g h i||w|
     * Z dvojrozmerneho pola uvw vsetky hodnoty prekonvertuje vynasobenim  matrix maticou 3x3
     * a vrati ziskane dvojrozmerne pole. Pozicie v poli sa zachovavaju
     * pre informaciu pozicie pixelu v obraze(prvy rozmer) a zlozku RGB(druhy rozmer)
     *
     */
    public double[] multiply(double[][] matrix, double[] uvw) {
        double x = 0;
        double y = 0;
        double z = 0;
        double tmp;
            for(int i = 0; i < 3; i++) {
                tmp = 0;
                for(int j = 0; j < 3; j++) {
                    tmp += matrix[i][j] * uvw[j];
                }
                switch (i) {
                    case 0: x = tmp;
                        break;
                    case 1: y = tmp;
                        break;
                    case 2: z = tmp;
                        break;
                }
            }
            uvw[0] = x;
            uvw[1] = y;
            uvw[2] = z;

        return uvw;
    }

    public double[] fromRGBtoLMS(double[] pixelData) {
        pixelData = multiply(MATRIX_RGBtoXYZ, pixelData);
        return multiply(MATRIX_XYZtoLMS, pixelData);
    }

    public double[] fromLMStoRGB(double[] pixelData) {
        pixelData = multiply(MATRIX_LMStoXYZ, pixelData);
        return multiply(MATRIX_XYZtoRGB, pixelData);
    }
}
