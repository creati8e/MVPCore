package serg.chuprin.sample;

import android.app.Application;

import serg.chuprin.sample.di.AppComponent;

public class SampleApplication extends Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        appComponent = DaggerAppComponent.create();
    }
}
