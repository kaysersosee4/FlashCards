package com.example.kaysersose.fiszki.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class FiszkiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);

        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));

        Stetho.Initializer initializer = initializerBuilder.build();

        Stetho.initialize(initializer);


    }
}
