package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance;

import android.graphics.Bitmap;

/**
 * Created by Vladimira Hezelova on 6. 12. 2015.
 */
public abstract class Convertor {

    private Bitmap originalBitmap;
    private int width;
    private int height;
    private Bitmap convertedBitmap;

    public Convertor(Bitmap bitmap) {
        this.originalBitmap = bitmap;
        this.width = originalBitmap.getWidth();
        this.height = originalBitmap.getHeight();
        this.convertedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    }

    public void balanceWhite() {
        int value;
        float[] rgb = new float[3];

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                value = originalBitmap.getPixel(j,i);
                rgb = getRGBFromValue(value, rgb);
                rgb = removeColorCast(rgb);
                convertedBitmap.setPixel(j, i, getValueFromRGB(rgb));
            }
        }
    }

    public abstract float[] removeColorCast(float[] pixelData);


    public int getValueFromRGB(float[] rgb) {
        int R = (int) rgb[0];
        int G = (int) rgb[1];
        int B = (int) rgb[2];
        return ((R & 0xFF) << 16) | ((G & 0xFF) << 8)  | ((B & 0xFF));
    }

    public float[] getRGBFromValue(int value, float rgb[]) {
        rgb[0] = (value >> 16) & 0xff; //red
        rgb[1] = (value >>  8) & 0xff; //green
        rgb[2] = (value      ) & 0xff;  //blue
        return rgb;
    }

    public Bitmap getConvertedBitmap() {
        return convertedBitmap;
    }

}
