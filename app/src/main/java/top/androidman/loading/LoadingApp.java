package top.androidman.loading;

import android.app.Application;

import top.androidman.loadinglibary.Loading;


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
