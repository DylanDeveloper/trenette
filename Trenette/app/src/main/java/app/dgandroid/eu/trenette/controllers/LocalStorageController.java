package app.dgandroid.eu.trenette.controllers;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import app.dgandroid.eu.trenette.utils.Constants;
import app.dgandroid.eu.trenette.utils.Logger;

/**
 * Created by Duilio on 21/08/2017.
 */

public class LocalStorageController {

    static final int READ_BLOCK_SIZE = 100;

    public static String loadDataString(Context cw) {
        try {
            File file = cw.getFileStreamPath(Constants.LOCAL_DATA);
            if(file.exists()) {
                Logger.i("Exist...");
                FileInputStream fileIn= cw.openFileInput(Constants.LOCAL_DATA);
                InputStreamReader InputRead= new InputStreamReader(fileIn);
                char[] inputBuffer= new char[READ_BLOCK_SIZE];
                String s="";
                int charRead;
                while ((charRead=InputRead.read(inputBuffer))>0) {
                    // char to string conversion
                    String readstring=String.copyValueOf(inputBuffer,0,charRead);
                    s +=readstring;
                }
                InputRead.close();
                return s;
            } else {
                Logger.e("Not Exist...");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static void setSecureValue(String elem, Context cw) {
        FileOutputStream outputStream = null;
        try {
            outputStream = cw.openFileOutput(Constants.LOCAL_DATA, Context.MODE_PRIVATE);
            outputStream.write(elem.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}