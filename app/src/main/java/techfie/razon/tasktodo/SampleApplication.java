package techfie.razon.tasktodo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
//import android.support.multidex.MultiDex;
//import android.support.multidex.MultiDexApplication;


/**
 * Created by razon30 on 06-11-16.
 */

public class SampleApplication extends Application {
    public static SharedPreferences preferences;
    private static  SampleApplication sInstance;
    public static Context context;
    public static  SampleApplication getInstance(){

        return sInstance;

    }
    public static Context getAppContext(){

        return sInstance.getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = this;
        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);
    }
}
