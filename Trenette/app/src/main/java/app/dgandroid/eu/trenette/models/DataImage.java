package app.dgandroid.eu.trenette.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Duilio on 21/08/2017.
 */

public class DataImage extends Object implements Serializable {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("details")
    private String details;
    @SerializedName("imageURL")
    private String imageURL;

    public DataImage(String name, String details, long id, String imageURL){
        this.name           = name;
        this.id             = id;
        this.details        = details;
        this.imageURL       = imageURL;
    }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getImageURL() {
        return imageURL;
    }
}