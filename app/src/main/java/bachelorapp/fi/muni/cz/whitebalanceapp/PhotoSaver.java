package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.graphics.Bitmap;

/**
 * Created by Vladimira Hezelova on 5. 12. 2015.
 */
public class PhotoSaver {
    private String imagePath;
    private Bitmap selectedWhite;

    private Bitmap originalBitmap;
    private Bitmap convertedBitmap;

    public PhotoSaver(String imagePath, Bitmap selectedWhite) {
        this.imagePath = imagePath;
        this.selectedWhite = selectedWhite;
        //decodePhoto();
    }
/*
    public void decodePhoto() {
        originalBitmap = BitmapFactory.decodeFile(imagePath);
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        convertedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        PixelData pixelDataInstance = new PixelData();
        Conversion conversion = new Conversion();

        int value;
        double[][] pixelData = new double[1][3];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                value = originalBitmap.getPixel(j,i);
                pixelData[0][0] = (value >> 16) & 0xff; //red;
                pixelData[0][1] = (value >>  8) & 0xff; //green
                pixelData[0][2] = (value      ) & 0xff;  //blue

                pixelData = conversion.convert(0, 0, selectedWhite, pixelData);
                convertedBitmap.setPixel(j, i, getValue(pixelData));
            }
        }
    }
*/
    public int getValue(double[][] pixelData) {
        int R = (int) pixelData[0][0];
        int G = (int) pixelData[0][1];
        int B = (int) pixelData[0][2];
        return ((R & 0xFF) << 16) | ((G & 0xFF) << 8)  | ((B & 0xFF));
    }

    public Bitmap getConvertedBitmap() {
        return convertedBitmap;
    }
/*
    public void savePhoto() {
        String destinationFilename = getFilename();
        Log.i("destinationFilename", destinationFilename);

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destinationFilename, false);
            bos = new BufferedOutputStream(fos);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(new File(destinationFilename));
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            } else {
                sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }

            Toast.makeText(getApplicationContext(), R.string.save_message2, Toast.LENGTH_SHORT).show();
            //  convertedBitmaps[indexOfSelectedBitmap].recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getFilename() {
        Date date = new Date();
        String sDate = new SimpleDateFormat("yyyyMMdd_hhmmss").format(date);

        String path = new File(imagePath).getParent();

        String fileNameWithExtension = new File(imagePath).getName();
        String fileName = sDate;
        String extension = "jpeg";
        int pos = fileNameWithExtension.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileNameWithExtension.substring(0, pos);
            extension = fileNameWithExtension.substring(pos);
        }
        String destinationFilename = path + File.separatorChar + fileName + sDate + extension;
        Log.i("destinationFilename", destinationFilename);
        return destinationFilename;

    }
*/
}
