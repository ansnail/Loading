package top.androidman.loading;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import top.androidman.loading.databinding.ActivityMainBinding;
import top.androidman.loadinglibary.Loading;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    private ActivityMainBinding mBinding;

    private boolean image1Success;
    private boolean image2Success;

    private String picUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(mBinding.getRoot());
        Loading.getIns().target(this).show(Style.LOADING);
        Glide.with(this).load(ImageFactory.getNormalImage()).into(mBinding.ivImage1);
        Glide.with(this).load(ImageFactory.getNormalImage()).into(mBinding.ivImage2);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Loading.getIns().target(MainActivity.this).show(Style.SUCCESS);
            }
        }, 3000);
    }


    public void loadImage1(View view) {
        Loading.getIns().target(mBinding.ivImage1).show(Style.LOADING);

        Glide.with(this)
                .load(ImageFactory.getNormalImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Loading.getIns().target(mBinding.ivImage1).show(Style.SUCCESS);
                        return false;
                    }
                })
                .into(mBinding.ivImage1);
    }

    public void loadImage2(View view) {
        picUrl = ImageFactory.getErrorImage();
        loadData();
    }

    public void loadImageAll(View view) {
        image1Success = false;
        image2Success = false;
        Loading.getIns().target(mBinding.llContainer).show(Style.LOADING);
        Glide.with(this)
                .load(ImageFactory.getNormalImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        image1Success = true;
                        if (image2Success) {
                            Loading.getIns().target(mBinding.llContainer).show(Style.SUCCESS);
                        }
                        return false;
                    }
                })
                .into(mBinding.ivImage1);

        Glide.with(this)
                .load(ImageFactory.getNormalImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        image2Success = true;
                        if (image1Success) {
                            Loading.getIns().target(mBinding.llContainer).show(Style.SUCCESS);
                        }
                        return false;
                    }
                })
                .into(mBinding.ivImage2);
    }

    private void loadData() {
        Loading.getIns().target(mBinding.ivImage2).show(Style.LOADING);
        Glide.with(this)
                .load(picUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Loading.getIns().target(mBinding.ivImage2).show(Style.FAILED, new Loading.OnRetryListener() {
                            @Override
                            public void retry() {
                                picUrl = ImageFactory.getNormalImage();
                                loadData();
                            }
                        });
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Loading.getIns().target(mBinding.ivImage2).show(Style.SUCCESS);
                        return false;
                    }
                })
                .into(mBinding.ivImage2);
    }

    public void loadEmpty(View view) {
        Loading.getIns().target(mBinding.llContainer).show(Style.LOADING);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Loading.getIns().target(mBinding.llContainer).show(Style.NO_WIFI, new Loading.OnRetryListener() {
                    @Override
                    public void retry() {
                        Loading.getIns().target(mBinding.llContainer).show(Style.LOADING);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Loading.getIns().target(mBinding.llContainer).show(Style.EMPTY, new Loading.OnRetryListener() {
                                    @Override
                                    public void retry() {
                                        Toast.makeText(MainActivity.this, "空状态", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }, 3000);
                    }
                });
            }
        }, 3000);
    }
}
