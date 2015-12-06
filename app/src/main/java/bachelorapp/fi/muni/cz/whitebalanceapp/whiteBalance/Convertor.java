package bachelorapp.fi.muni.cz.whitebalanceapp.whiteBalance;

/**
 * Created by Vladimira Hezelova on 6. 12. 2015.
 */
public class Convertor {



    public Convertor() {
    }



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

}
