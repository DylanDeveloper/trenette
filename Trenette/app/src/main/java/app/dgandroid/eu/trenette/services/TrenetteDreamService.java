package app.dgandroid.eu.trenette.services;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.service.dreams.DreamService;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import app.dgandroid.eu.trenette.R;
import app.dgandroid.eu.trenette.controllers.DataStorageController;
import app.dgandroid.eu.trenette.customs.TrenetteKenBurnsView;
import app.dgandroid.eu.trenette.customs.TrenetteLoopViewPager;
import app.dgandroid.eu.trenette.interfaces.StorageDelegateInterface;
import app.dgandroid.eu.trenette.models.DataImage;
import app.dgandroid.eu.trenette.models.ImageBundle;
import app.dgandroid.eu.trenette.tasks.DownloadImagesTask;
import app.dgandroid.eu.trenette.utils.Logger;
import app.dgandroid.eu.trenette.utils.SampleImages;
import app.dgandroid.eu.trenette.utils.Utility;

/**
 * Created by Duilio on 21/08/2017.
 */

public class TrenetteDreamService extends DreamService {

    private ContextWrapper context;
    private List<ImageBundle> arrayList; //List of images downloaded from server and ready to use!
    private DownloadImagesTask downloadImagesTask; //Fake task for simulate call on web service in background!
    private Timer timer; //Timer Task in loop for doing calls (checking probably new Bundle!
    private TextView imageNameText, detailsImage; //our own textViews for showing every name and details for every image showed!
    private TrenetteKenBurnsView trenetteKenBurnsView; //My own Custom View for showing the amazing KenBurns effect!

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        setContentView(R.layout.trenette_dream_service);
        imageNameText           = (TextView) findViewById(R.id.nameImage);
        detailsImage            = (TextView) findViewById(R.id.detailsImage);
        trenetteKenBurnsView    = (TrenetteKenBurnsView) findViewById(R.id.ken_burns_view);
        context                 = this;
        arrayList               = new ArrayList<ImageBundle>();
        //Before download new Images need to show the previus one saved on device, if not images saved let's show the standard set stored in app.
        DataStorageController.getInstance().readData(this, new StorageDelegateInterface() {
            @Override
            public void onGetDataFromStorage(List<DataImage> dataImages) { //List of DataImage class that contains parameters saved like name, details, url and id.
                Logger.i("dataImages == " + dataImages.size());
                for(int i = 0; i < dataImages.size(); i++) {
                    Bitmap bitmap = Utility.loadImageBitmap(context, dataImages.get(i).getName()); //Let's load the bitmap saved in our own files
                    arrayList.add(new ImageBundle(dataImages.get(i).getName(), dataImages.get(i).getDetails(), dataImages.get(i).getId() , bitmap));
                }
                initializeKenBurnsView(arrayList, false); //Let's initialize our images downloaded before and stored!
            }
            @Override
            public void onGetDataNull() { //show default images stored in app cause it's the first time that you opened this App!
                Logger.i("dataImages == null");
                initializeKenBurnsView(null, true); //Let's initialize our default bundle images...
            }
        });
        //download images from server (Fake)...
        timer = new Timer();
        timer.schedule( new TimerTask() { //Let's start our Timer Task in background for looping calls every each sec...
            public void run() {
                Logger.i("TimerTask  ***** ");
                callAction(); //Call the fake web service!
            }
        }, 20000, 20000); //60 sec for starting the first call, 60 sec for every loop.
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false); //Allow user touch
        setFullscreen(true);  //Hide system UI
    }

    public void callAction(){
        Logger.i("callAction test...");
        downloadImagesTask = new DownloadImagesTask(context, new DownloadImagesTask.CallbackData() {
            @Override
            public void onFakeResponseFromServer(List<ImageBundle> dataImages) {
                if(dataImages != null) {
                    arrayList = dataImages;
                    initializeKenBurnsView(arrayList, false); //Initialize our Custom View with the new Bundle images!
                }
            }
        });
        downloadImagesTask.execute();
    }

    private void initializeKenBurnsView(final List<ImageBundle> arrayList, final boolean isEmptyData) {
        Logger.i("initializeKenBurnsView...");
        trenetteKenBurnsView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        trenetteKenBurnsView.setSwapMs(8000); //sec for every image!
        trenetteKenBurnsView.setFadeInOutMs(600); //time for fading out transition for every image!
        TrenetteLoopViewPager trenetteLoopViewPager = null;

        TrenetteLoopViewPager.LoopViewPagerListener listener = new TrenetteLoopViewPager.LoopViewPagerListener() {
            @Override
            public View OnInstantiateItem(int page) {
                if(arrayList == null) {
                    imageNameText.setText(SampleImages.IMAGES_NAME[page]); //Standard set
                    detailsImage.setText(SampleImages.IMAGES_DETAILS[page]);
                }
                return null;
            }
        };
        if(isEmptyData) { //if empty data let's show our default images bundle!
            Logger.i("isEmptyData...");
            List<Integer> resourceIDs = Arrays.asList(SampleImages.IMAGES_RESOURCE);
            trenetteKenBurnsView.loadResourceIDs(resourceIDs);
            trenetteLoopViewPager = new TrenetteLoopViewPager(this, resourceIDs.size(), listener);
        } else  { //show the bundle downloaded or stored...
            Logger.i("okay data...");
            trenetteKenBurnsView.loadObjects(arrayList, imageNameText, detailsImage);
            trenetteLoopViewPager = new TrenetteLoopViewPager(this, arrayList.size(), listener);
        }
        FrameLayout viewPagerFrame = (FrameLayout) findViewById(R.id.view_pager_frame);
        viewPagerFrame.addView(trenetteLoopViewPager);
        trenetteKenBurnsView.setPager(trenetteLoopViewPager); //Set pager for creating the page transition Loop!
    }
}