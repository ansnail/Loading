package top.androidman.loading;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yanjie
 * @version 1.0
 * @date 2019/4/16 上午10:31
 * @description
 */
public final class Style {

    ///////////////////////////////////////////////////////////////////////////
    // loading的各种状态开始
    ///////////////////////////////////////////////////////////////////////////

    public static final int LOADING = 0x1;
    public static final int SUCCESS = 0x2;
    public static final int FAILED = 0x3;
    public static final int EMPTY = 0x4;
    public static final int NO_WIFI = 0x5;

    @IntDef({LOADING, SUCCESS, FAILED, EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Loading {
    }

    ///////////////////////////////////////////////////////////////////////////
    // loading的各种状态结束
    ///////////////////////////////////////////////////////////////////////////


}
