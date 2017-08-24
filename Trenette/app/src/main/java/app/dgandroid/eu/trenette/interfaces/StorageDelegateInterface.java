package app.dgandroid.eu.trenette.interfaces;

import java.util.List;
import app.dgandroid.eu.trenette.models.DataImage;

/**
 * Created by Duilio on 21/08/2017.
 */

public interface StorageDelegateInterface {

    void onGetDataFromStorage(List<DataImage> dataImages);
    void onGetDataNull();

}
