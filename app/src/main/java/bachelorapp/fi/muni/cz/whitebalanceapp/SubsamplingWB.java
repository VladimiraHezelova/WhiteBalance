package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Random;

/**
* Created by Vladimira Hezelova on 20. 8. 2015.
        */
public class SubsamplingWB {


    public static Bitmap conversion(Bitmap image) {

        int n = 50;
        int m = 10;

        Log.e("bitmap", image.toString());
        int rows = image.getHeight();
        int cols = image.getWidth();
        double[] illuminationEstimation = performIlluminationEstimation(image, n, m);

        Bitmap result = Bitmap.createBitmap(cols, rows, Bitmap.Config.ARGB_8888);
        result = removeColorCast(image, result, illuminationEstimation);

        return result;

    }
/*
    public BufferedImage readImage(String path) throws IOException {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Can't read input file!");
            throw e;
        }
    }

    public void writeImage(BufferedImage image, String path, String type) {
        try {
            File outputfile = new File(path + "." + type);
            ImageIO.write(image, type, outputfile);
        } catch (Exception ex) {
          //  JOptionPane.showMessageDialog(null, "You haven't created any image to save.");
            System.err.println("Can't write to output file!");
        }
    }
*/
    public  static double[] performIlluminationEstimation(Bitmap image, int n, int m) {
        Bitmap source = image.copy(image.getConfig(), true);
        int rows = image.getHeight();
        int cols = image.getWidth();

        double[] result = new double[]{0.0,0.0,0.0,0.0};


        Random randD = new Random();
        for(int i = 0; i < m; i++) {
            double[] max = new double[]{0.0,0.0,0.0,0.0};
            for(int j = 0; j < n; j++) {

                //double randomDouble1 = 1.0 * randD.nextDouble();
                //double randomDouble2 = 1.0 * randD.nextDouble();

                double start = 0.0;
                double end = 1.0;
                double random = new Random().nextDouble();
                double resultRandom = start + (random * (end - start));

              //  double p1 = resultRandom;

                random = new Random().nextDouble();
                resultRandom = start + (random * (end - start));

              //  double p2 = resultRandom;


                double p1 = 0.030282793263732271;
                double p2 = 0.90105944297145701;

                int row=(int)((rows-1)*p1);
                int col=(int)((cols-1)*p2);
                int pointI = source.getPixel(col, row);
                double[] point = getPixelDataFromValue(pointI);
                for(int k = 0; k < 3; k++) {
                    if(max[k] < point[k]) {
                        max[k] = point[k];
                    }
                }
            }
            result[0] += max[0];
            result[1] += max[1];
            result[2] += max[2];
        }
        double c=result[0];
        result[0]=result[2];
        result[2]=c;
        double sum=result[0]*result[0]+result[1]*result[1]+result[2]*result[2];

        sum/=3;
        sum=Math.sqrt(sum);

        result[0] /= sum;
        result[1] /= sum;
        result[2] /= sum;

        return result;
    }

    public static double[] getPixelDataFromValue(int value) {
        double[] rgb = new double[3];
        rgb[2] = (value >> 16) & 0xff; //red
        rgb[1] = (value >>  8) & 0xff; //green
        rgb[0] = (value      ) & 0xff;  //blue
        return rgb;
    }

    public static int getValueFromPixelData(double[] pixelData) {
        return (int)pixelData[0] | ((int)pixelData[1] << 8) | ((int)pixelData[2] << 16);
    }
/*
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

*/
    public static Bitmap removeColorCast(Bitmap source, Bitmap destination, double[] illuminationEstimation) {

        int rows=source.getHeight();
        int cols=source.getWidth();

        Bitmap converted = Bitmap.createBitmap(cols, rows, Bitmap.Config.ARGB_8888);

        for (int i=0;i<rows;++i){
            for (int j=0;j<cols;++j){
                int inputI = source.getPixel(j, i);
                double[] input = getPixelDataFromValue(inputI);
                double a = input[0];
                double b = input[1];
                double[] point = {input[0],input[1],input[2]};
                for (int k=0;k<3;++k){
                    point[k]/=illuminationEstimation[2-k];
                }
                for(int k = 0; k < 3; k++) {
                    if(point[k] > 255) {
                        point[k] = 255;
                    }
                }
                int rgb = getIntFromColor((int)point[0],(int)point[1],(int)point[2]);
                converted.setPixel(j, i, rgb);
            }
        }
        return converted;
    }

    public static int getIntFromColor(int Red, int Green, int Blue){
        Blue = (Blue << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Red = Red & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

}
