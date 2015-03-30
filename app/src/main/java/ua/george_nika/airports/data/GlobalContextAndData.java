package ua.george_nika.airports.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class GlobalContextAndData extends Application {

    private static final String NAME_PREFERENCES = "airports_program_preferences";
    private static final String ZOOM_ON_MAP_PREFS = "zoom_on_map_prefs";
    private static final Integer defaultZoom = 8;
    private static final Integer maxZoom = 15;
    private static final String NEAREST_IN_KM_PREFS = "nearest_in_km_prefs";
    private static final Integer defaultNearestKm = 150;
    private static final Integer maxNearestKm = 300;

    private static Context context;
    private static Integer zoomOnMap;
    private static Integer kmToNearestAirports;
    private static SharedPreferences sharedPrefs;


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

        sharedPrefs = getSharedPreferences(NAME_PREFERENCES, MODE_PRIVATE);
        zoomOnMap = sharedPrefs.getInt(ZOOM_ON_MAP_PREFS, defaultZoom);
        kmToNearestAirports = sharedPrefs.getInt(NEAREST_IN_KM_PREFS, defaultNearestKm);
    }

    public static Integer getKmToNearestAirports() {
        return kmToNearestAirports;
    }

    public static void setKmToNearestAirports(Integer kmToNearestAirports) {
        GlobalContextAndData.kmToNearestAirports = kmToNearestAirports;
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(NEAREST_IN_KM_PREFS, kmToNearestAirports);
        editor.apply();
    }

    public static Integer getZoomOnMap() {
        return zoomOnMap;
    }

    public static void setZoomOnMap(Integer zoomOnMap) {
        GlobalContextAndData.zoomOnMap = zoomOnMap;
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(ZOOM_ON_MAP_PREFS, zoomOnMap);
        editor.apply();
    }

    public Integer getDefaultZoom(){
        return defaultZoom;
    }
    public Integer getDefaultNearestKm(){
        return defaultNearestKm;
    }


    public static Integer getMaxZoom() {
        return maxZoom;
    }

    public static Integer getMaxNearestKm() {
        return maxNearestKm;
    }


}
