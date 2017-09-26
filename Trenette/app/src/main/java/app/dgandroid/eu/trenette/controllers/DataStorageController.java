package app.dgandroid.eu.trenette.controllers;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import app.dgandroid.eu.trenette.interfaces.StorageDelegateInterface;
import app.dgandroid.eu.trenette.models.BundleDataImages;
import app.dgandroid.eu.trenette.models.DataImage;
import app.dgandroid.eu.trenette.utils.Logger;

/**
 * Created by Duilio on 21/08/2017.
 */

public class DataStorageController {

    private String SAVE_DATA_STRING;
    private static DataStorageController mInstance;
    private BundleDataImages mBundleDataImages;

    public static DataStorageController getInstance(){
        if(mInstance == null) {
            mInstance = new DataStorageController();
        }
        return mInstance;
    }

    public BundleDataImages getBundleDataImages(){
        if(mBundleDataImages == null) {
            mBundleDataImages = new BundleDataImages();
        }
        return mBundleDataImages;
    }

    public DataImage getDataImageWithID(long id){ //Future use, if maybe we want to delete a specific image or modify etc...
        for (DataImage dataImage : mBundleDataImages.getImages()) {
            if(dataImage.getId() == id) {
                return dataImage;
            }
        }
        return  null;
    }

    public void readData(Context context, StorageDelegateInterface delegateInterface) {
        SAVE_DATA_STRING = LocalStorageController.loadDataString(context); //Read data from interal storage
        Logger.i("SAVE_DATA_STRING = " + SAVE_DATA_STRING);
        Gson gson = new GsonBuilder().create();
        mBundleDataImages = gson.fromJson(SAVE_DATA_STRING, BundleDataImages.class); //Transformation from string file to BundleDataImages class!
        if(mBundleDataImages != null) {
            Logger.e("mBundleDataImages...");
            delegateInterface.onGetDataFromStorage(mBundleDataImages.getImages());
        } else {
            Logger.e("null...");
            delegateInterface.onGetDataNull();
        }
    }

    public void updateData(Context context) {
        Gson gson = new Gson();
        String s = gson.toJson(mBundleDataImages);
        LocalStorageController.saveData(s, context);
        Logger.i(s);
    }
}