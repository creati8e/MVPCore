package serg.chuprin.sample;

import android.app.Application;

import serg.chuprin.sample.common.di.AppComponent;
import serg.chuprin.sample.common.di.DaggerAppComponent;

public class SampleApplication extends Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.create();
    }
}