package app.dgandroid.eu.trenette.models;

import android.graphics.Bitmap;

/**
 * Created by Duilio on 22/08/2017.
 */

public class ImageBundle {
    private String name;
    private String details;
    private long id;
    private Bitmap bitmap;

    public ImageBundle(String name, String details, long id, Bitmap bitmap) {
        this.name       = name;
        this.details    = details;
        this.id         = id;
        this.bitmap     = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public long getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public String getName() {
        return name;
    }
}