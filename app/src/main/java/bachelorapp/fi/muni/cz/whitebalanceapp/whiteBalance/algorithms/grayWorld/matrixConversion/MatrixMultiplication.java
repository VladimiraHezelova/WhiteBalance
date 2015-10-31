package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance.algorithms.grayWorld.matrixConversion;

/**
 * Created by Vladimira Hezelova on 16. 3. 2015.
 */


public class MatrixMultiplication extends Thread {

    private int thread;
    private int start;
    private int end;
    private double[][] matrix;
    private double[][] uvw;

    /**
     *
     * @param thread vlakno
     * @param start start
     * @param end end
     * @param matrix  matica 3x3
     * @param uvw dvojrozmerne pole, prvy rozmer urcuje poradie pixelu v obraze, druhy zlozku rgb ([0]=red,[1]=green,[2]=blue)
     */
    public MatrixMultiplication(int thread, int start, int end, double[][] matrix, double[][] uvw) {
        this.thread = thread;
        this.start = start;
        this.end = end;
        this.matrix = matrix;
        this.uvw = uvw;
    }
    /**
     * |x| |a b c||u|
     * |y|=|d e f||v|
     * |z| |g h i||w|
     * Z dvojrozmerneho pola uvw vsetky hodnoty prekonvertuje vynasobenim  matrix maticou 3x3
     * a vrati ziskane dvojrozmerne pole. Pozicie v poli sa zachovavaju
     * pre informaciu pozicie pixelu v obraze(prvy rozmer) a zlozku RGB(druhy rozmer)
     *
     */
    public void run() {
        double x = 0;
        double y = 0;
        double z = 0;
        double tmp;
        for(int k = start; k < end; k++){
            for(int i = 0; i < 3; i++) {
                tmp = 0;
                for(int j = 0; j < 3; j++) {
                    tmp += matrix[i][j] * uvw[k][j];
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
            uvw[k][0] = x;
            uvw[k][1] = y;
            uvw[k][2] = z;
        }
    }
    public double[][] getPixelData() {
        return uvw;
    }
}