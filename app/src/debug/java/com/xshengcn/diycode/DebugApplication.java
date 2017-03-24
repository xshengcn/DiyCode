package com.xshengcn.diycode;

import com.facebook.stetho.Stetho;

public class DebugApplication extends DiyCodeApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
