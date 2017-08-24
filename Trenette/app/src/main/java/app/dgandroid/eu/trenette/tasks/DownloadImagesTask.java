package app.dgandroid.eu.trenette.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import java.util.List;
import app.dgandroid.eu.trenette.controllers.DataStorageController;
import app.dgandroid.eu.trenette.models.ImageBundle;
import app.dgandroid.eu.trenette.utils.FakeServerBundle;
import app.dgandroid.eu.trenette.utils.Logger;
import app.dgandroid.eu.trenette.utils.Utility;

/**
 * Created by Duilio on 22/08/2017.
 */

public class DownloadImagesTask extends AsyncTask<Context, ProgressDialog, List<ImageBundle>> {

    private Context context;
    private CallbackData listner;

    public DownloadImagesTask(Context context, CallbackData listner) {
        this.context    = context;
        this.listner    = listner;
    }

    @Override
    protected List<ImageBundle> doInBackground(Context... params) {
        List<ImageBundle> dataImages = null;
        //Fake response from Server...
        if(DataStorageController.getInstance().getBundleDataImages().getVersionBundle() >= FakeServerBundle.VERSION_BUNDLE ) {
            Logger.i("The version on the server is less than this so let's proceed without update nothing.");
            return null;
        } else {
            Logger.i("Let's update the Bundle Images");
            dataImages = Utility.saveDataImages(context);
            return dataImages;
        }
    }

    @Override
    protected void onPostExecute(List<ImageBundle> dataImages) {
        super.onPostExecute(dataImages);
        listner.onFakeResponseFromServer(dataImages);
    }

    public interface CallbackData {
        void onFakeResponseFromServer(List<ImageBundle> dataImages);
    }
}