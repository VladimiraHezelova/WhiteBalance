package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.partialConversions;

/**
 * Created by Vladimira Hezelova on 5. 12. 2015.
 */
public class MatrixMultiplication1D {
    // matrix for conversion from normalized, linearized RGB to CIE D65 XYZ
    private static final float[][] MATRIX_RGBtoXYZ =
            {{0.4124f, 0.3576f, 0.1805f},
                    {0.2126f, 0.7152f, 0.0722f},
                    {0.0193f, 0.1192f, 0.9505f}};

    // matrix for conversion from CIE D65 XYZ to LMS (tristimulus values of the Long, Medium, Short)
    private static final float[][] MATRIX_XYZtoLMS =
            {{0.7982f, 0.3389f, -0.1371f},
                    {-0.5918f, 1.5512f, 0.0406f},
                    {0.0008f, 0.0239f, 0.9753f}};

    // inverse matrix to XYZtoLMS
    // matrix for conversion from LMS to CIE D65 XYZ
    private static final float[][] MATRIX_LMStoXYZ =
            {{1.07645f,-0.23766f, 0.16121f},
                    {0.41096f, 0.55434f, 0.03469f},
                    {-0.01095f, -0.01338f, 1.02434f}};

    // inverse matrix to RGBtoXYZ
    // matrix for conversion from CIE D65 XYZ to RGB
    private static final float[][] MATRIX_XYZtoRGB =
            {{3.24062f, -1.53720f, -0.49862f},
                    {-0.96893f, 1.87575f, 0.04151f},
                    {0.05571f, -0.20402f, 1.05699f}};



    /**
     * |x| |a b c||u|
     * |y|=|d e f||v|
     * |z| |g h i||w|
     * Z dvojrozmerneho pola uvw vsetky hodnoty prekonvertuje vynasobenim  matrix maticou 3x3
     * a vrati ziskane dvojrozmerne pole. Pozicie v poli sa zachovavaju
     * pre informaciu pozicie pixelu v obraze(prvy rozmer) a zlozku RGB(druhy rozmer)
     *
     */
    public float[] multiply(float[][] matrix, float[] uvw) {
        float[] abc = {0.0f, 0.0f, 0.0f};
        for(int i = 0; i < 3; i++) {
            float tmp = 0;
            for(int j = 0; j < 3; j++) {
                tmp += matrix[i][j] * uvw[j];
            }
            abc[i] = tmp;
        }
        uvw[0] = abc[0];
        uvw[1] = abc[1];
        uvw[2] = abc[2];
        return uvw;
    }

    public float[] fromRGBtoLMS(float[] pixelData) {
        pixelData = multiply(MATRIX_RGBtoXYZ, pixelData);
        return multiply(MATRIX_XYZtoLMS, pixelData);
    }

    public float[] fromLMStoRGB(float[] pixelData) {
        pixelData = multiply(MATRIX_LMStoXYZ, pixelData);
        return multiply(MATRIX_XYZtoRGB, pixelData);
    }
}
