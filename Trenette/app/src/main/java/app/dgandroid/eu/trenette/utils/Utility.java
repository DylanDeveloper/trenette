package app.dgandroid.eu.trenette.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import app.dgandroid.eu.trenette.controllers.DataStorageController;
import app.dgandroid.eu.trenette.models.DataImage;
import app.dgandroid.eu.trenette.models.ImageBundle;

/**
 * Created by Duilio on 22/08/2017.
 */

public class Utility {

    public static List<ImageBundle> saveDataImages(Context context) { //Simulate Fake response and getting data...
        DataStorageController.getInstance().getBundleDataImages().clearImages();
        ArrayList<ImageBundle> arrayList = new ArrayList<ImageBundle>();
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        for(int i = 0; i< FakeServerBundle.IMAGES_IDS.length; i++) {
            Logger.i("dataImages name *** " + i + " *** " + FakeServerBundle.IMAGES_NAME[i]);
            Bitmap image = null;
            try {
                image = Glide.with(context).load(FakeServerBundle.IMAGES_URLS[i]).asBitmap().into(-1,-1).get(); //-1 , -1 save the original quality!
                bitmaps.add(image);
            } catch (Exception e) {
                Logger.e("Exception = " + e.getMessage());
                return null; //if some of these images is corrupted, return null and Do not set the current Bundle, try next time maybe with a good connection...
            }
            DataStorageController.getInstance().getBundleDataImages().
                    addDataImageInList(new DataImage(FakeServerBundle.IMAGES_NAME[i], FakeServerBundle.IMAGES_DETAILS[i],
                            FakeServerBundle.IMAGES_IDS[i], FakeServerBundle.IMAGES_URLS[i])); //Save for cache or local storage

            arrayList.add(new ImageBundle(FakeServerBundle.IMAGES_NAME[i], FakeServerBundle.IMAGES_DETAILS[i],
                    FakeServerBundle.IMAGES_IDS[i], image));
        }

        trimCache(context); //delete cache
        clearMyFiles(context); //delete old Bundle Images

        for(int i= 0; i<bitmaps.size(); i++) {
            saveImage(context, bitmaps.get(i) , FakeServerBundle.IMAGES_NAME[i]); //save the new images
        }

        DataStorageController.getInstance().getBundleDataImages().setVersionBundle(FakeServerBundle.VERSION_BUNDLE); //set the new Bundle Version!
        DataStorageController.getInstance().updateData(context); //Save all DataImage received from server in our JSON string data file!
        Logger.i("----- on return data...");
        return arrayList; // return the new Bundle Images!
    }

    public static void trimCache(Context context) {
        File dir = context.getCacheDir();
        if(dir!= null && dir.isDirectory()){
            File[] children = dir.listFiles();
            if (children == null) {
                // Either dir does not exist or is not a directory
                Logger.e("children == null...");
            } else {
                File temp;
                for (int i = 0; i < children.length; i++) {
                    Logger.e("children.length to delete = " + children.length);
                    temp = children[i];
                    temp.delete();
                }
            }
        }
    }

    public static void clearMyFiles(Context context) {
        File[] files = context.getFilesDir().listFiles();
        if(files != null) {
            Logger.e("files..." + files.length);
            for (File file : files) {
                Logger.e("file = " + file.getName());
                file.delete();
            }
        } else  {
            Logger.e("No files...");
        }
    }

    public static void saveImage(Context context, Bitmap b, String imageName)
    {
        FileOutputStream foStream;
        try
        {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 100, foStream);
            foStream.close();
        }
        catch (Exception e)
        {
            Logger.i("Exception 2, Something went wrong! " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Logger.i("Exception 3, Something went wrong! " + e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }
}