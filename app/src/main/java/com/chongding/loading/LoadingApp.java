package com.chongding.loading;

import android.app.Application;

import com.chongding.loadinglibary.Loading;


/**
 * @author yanjie
 * @version 1.0
 * @date 2019-06-21 17:46
 * @description
 */
public class LoadingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Loading.getIns().setAdapter(new DefaultLoadingAdapter(this));
    }
}
