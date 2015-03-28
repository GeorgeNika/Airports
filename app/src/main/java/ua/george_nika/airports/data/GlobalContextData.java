package ua.george_nika.airports.data;

import android.app.Application;
import android.content.Context;

public class GlobalContextData extends Application {

    private static Context context;

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
