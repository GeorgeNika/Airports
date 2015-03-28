package ua.george_nika.airports.data;

import android.app.Application;
import android.content.Context;

public class GlobalContextData extends Application {

    private static Context context;
    private static final String API_KEY = "AIzaSyC0MpmIFTE1AJqk3XAt1DRmsZZXfzvnStQ";
    private static final String KEY_TYPE = "Android applications EC:51:A2:04:B1:48:A1:B2:F9:0F:E3:EA:69:81:DA:0E:CB:7F:9A:B1;ua.george_nika.airports";
    private static final String ACTIVATION_DATA = " Mar 28, 2015, 11:40:00 AM";
    private static final String ACTIVATION_OWNER = "georgerobocatnika@gmail.com ";

    private static final String SHA1 = "EC:51:A2:04:B1:48:A1:B2:F9:0F:E3:EA:69:81:DA:0E:CB:7F:9A:B1";
    private static final String PROJECT_NUMBER = "900947283261";
    private static final String PROJECT_ID = "airports-george-898381";
    private static final String PROJECT_NAME = "Airports";

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getApplicationContext())
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .diskCacheSize(40 * 1024 * 1024)
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs()
//                .build();
//        ImageLoader.getInstance().init(config);

       context = getApplicationContext();
    }
}
