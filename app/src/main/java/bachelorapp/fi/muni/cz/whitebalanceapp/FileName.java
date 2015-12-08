package bachelorapp.fi.muni.cz.whitebalanceapp;

import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vladimira Hezelova on 8. 12. 2015.
 */
public class FileName {
    private String imagePath;
    private String fileName;
    private String sDate;
    private String path;
    private String extension;


    public FileName(String imagePath) {
        this.imagePath = imagePath;

        Date date = new Date();
        sDate = new SimpleDateFormat("yyyyMMdd_hhmmss").format(date);

        path = new File(imagePath).getParent();

        String fileNameWithExtension = new File(imagePath).getName();
        fileName = sDate;
        extension = "jpeg";
        int pos = fileNameWithExtension.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileNameWithExtension.substring(0, pos);
            extension = fileNameWithExtension.substring(pos);
        }
    }
    public String getDestinationFilename() {
        String destinationFilename = path + File.separatorChar + fileName + sDate + extension;
        Log.i("destinationFilename", destinationFilename);
        return destinationFilename;
    }
    public String getNewFilename() {
        String newFilename = fileName + sDate;
        Log.i("newFilename", newFilename);
        return newFilename;
    }
}
