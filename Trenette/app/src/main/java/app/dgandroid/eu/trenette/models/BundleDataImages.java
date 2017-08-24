package app.dgandroid.eu.trenette.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duilio on 21/08/2017.
 */

public class BundleDataImages {
    @SerializedName("dataImages")
    private List<DataImage> dataImages;
    @SerializedName("versionBundle")
    private int versionBundle;

    public BundleDataImages(){
        dataImages = new ArrayList<DataImage>();
    }

    public void addDataImageInList(DataImage dataImage){
        this.dataImages.add(dataImage);
    }

    public int getVersionBundle() {
        return versionBundle;
    }

    public void setVersionBundle(int versionBundle) {
        this.versionBundle = versionBundle;
    }

    public List<DataImage> getImages(){
        return this.dataImages;
    }

    public void clearImages() {
        if(dataImages.size() != 0) {
            dataImages.clear();
        }
    }
}