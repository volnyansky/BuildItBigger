package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Stas on 31.01.16.
 */
public class ThisApplication extends android.app.Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
