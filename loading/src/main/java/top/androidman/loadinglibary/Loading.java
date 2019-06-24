package top.androidman.loadinglibary;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yanjie
 */
public class Loading {
    private static final Loading OUR_INSTANCE = new Loading();

    /**
     * 存储Object和ViewHolder的Map
     */
    private Map<Object, ViewHolder> mObjectViewHolderMap = new HashMap<>();

    public static Loading getIns() {
        return OUR_INSTANCE;
    }

    private Loading() {}

    private Adapter mAdapter;
    private ViewHolder mViewHolder;

    /**
     * 设置自定义Adapter
     */
    public Loading setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    public ViewHolder target(Activity activity) {
        mViewHolder = mObjectViewHolderMap.get(activity);
        if (mViewHolder == null) {
            ViewGroup target = activity.findViewById(android.R.id.content);
            mViewHolder = new ViewHolder(mAdapter, target);
            mObjectViewHolderMap.put(activity, mViewHolder);
        }
        return mViewHolder;
    }

    public ViewHolder target(View target) {
        mViewHolder = mObjectViewHolderMap.get(target);
        if (mViewHolder == null) {
            //创建一个ViewGroup布局用来包裹目标view
            FrameLayout needWrapView = new FrameLayout(target.getContext());
            //获取目标view的布局参数，然后设置给包裹布局
            ViewGroup.LayoutParams lp = target.getLayoutParams();
            if (lp != null) {
                needWrapView.setLayoutParams(lp);
            }
            //假如目标布局有父布局，用包裹布局替换掉目标布局的位置
            if (target.getParent() != null) {
                ViewGroup parent = (ViewGroup) target.getParent();
                int index = parent.indexOfChild(target);
                parent.removeView(target);
                parent.addView(needWrapView, index);
            }
            //把目标布局添加到包裹布局
            needWrapView.addView(target, lp);

            mViewHolder = new ViewHolder(mAdapter, needWrapView);
            mObjectViewHolderMap.put(target, mViewHolder);
        }
        return mViewHolder;
    }

    /**
     * Loading适配器
     */
    public abstract static class Adapter {
        /**
         * 根据type创建不同的ViewHolder
         */
        public abstract View generateView(int viewType);
    }

    public static class ViewHolder {
        private Adapter mAdapter;
        //状态和包装完成的view的缓存队列
        private SparseArray<View> mStatusViews = new SparseArray<>();
        //需要包装的view
        private ViewGroup mNeedWrapView;
        //上一次的状态view
        private View mPreStatusView;

        public ViewHolder(Adapter adapter, ViewGroup needWrapView) {
            mAdapter = adapter;
            mNeedWrapView = needWrapView;
        }

        /**
         * 通过类型展示不同的界面
         */
        public void show(int viewType) {
            show(viewType, null);
        }

        /**
         * 通过类型展示不同的界面
         */
        public void show(int viewType, final OnRetryListener listener) {
            View statusView = mStatusViews.get(viewType);
            if (statusView == null) {
                statusView = mAdapter.generateView(viewType);
            }

            if (listener != null) {
                statusView.setEnabled(true);
                statusView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.retry();
                    }
                });
            } else {
                statusView.setEnabled(false);
            }

            if (statusView != mPreStatusView || mNeedWrapView.indexOfChild(statusView) < 0) {
                //移除上一次的状态view
                if (mPreStatusView != null) {
                    mNeedWrapView.removeView(mPreStatusView);
                }
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                mNeedWrapView.addView(statusView, lp);
            } else if (mNeedWrapView.indexOfChild(statusView) != mNeedWrapView.getChildCount() - 1) {
                statusView.bringToFront();
            }
            mPreStatusView = statusView;
            mStatusViews.put(viewType, statusView);
        }
    }


    public interface OnRetryListener {
        /**
         * 点击重试
         */
        void retry();
    }
}
