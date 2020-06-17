package com.example.twitterclone;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Xx9WFKDzByskkr9h5YmSnt0DxLlBoW1FBzhmpF6L")
                // if defined
                .clientKey("F8EVuJWWHVbE3llbT742umlxIbxCt5XIrBDpGoxr")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
