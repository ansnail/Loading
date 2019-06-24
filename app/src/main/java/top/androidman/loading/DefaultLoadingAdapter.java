package top.androidman.loading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import top.androidman.loadinglibary.Loading;

/**
 * @author yanjie
 * @version 1.0
 * @date 2019-04-28 16:38
 * @description 默认全局Adapter，
 */
public class DefaultLoadingAdapter extends Loading.Adapter {

    private Context mContext;

    public DefaultLoadingAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public View generateView(int viewType) {
        View defaultLoadingView = LayoutInflater.from(mContext).inflate(R.layout.layout_default_loading_view, null);
        ImageView image = defaultLoadingView.findViewById(R.id.image);
        TextView text = defaultLoadingView.findViewById(R.id.text);
        int imageResource = -1;
        String textResource = "";

        defaultLoadingView.setVisibility(View.VISIBLE);
        switch (viewType) {
            case Style.LOADING:
                imageResource = R.drawable.loading;
                textResource = "正在加载。。。";
                break;
            case Style.SUCCESS:
                defaultLoadingView.setVisibility(View.GONE);
                break;
            case Style.EMPTY:
                imageResource = R.drawable.icon_empty;
                textResource = "没有内容哦。。。";
                break;
            case Style.FAILED:
                imageResource = R.drawable.icon_failed;
                textResource = "加载失败。。。";
                break;
            case Style.NO_WIFI:
                imageResource = R.drawable.icon_no_wifi;
                textResource = "网络出错啦，点击重试。。。";
                break;
            default:
                break;
        }
        image.setImageResource(imageResource);
        text.setText(textResource);

        return defaultLoadingView;
    }
}
