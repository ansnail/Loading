package top.androidman.loading;

import java.util.Locale;

/**
 * @author yanjie
 * @version 1.0
 * @date 2019-05-09 16:40
 * @description
 */
public class ImageFactory {

    public static String getErrorImage() {
        return "http://www." + System.currentTimeMillis() + ".com/abc.png";
    }

    public static String getNormalImage() {
        int id = (int) (Math.random() * 100000);
        return String.format(Locale.CHINA, "https://www.thiswaifudoesnotexist.net/example-%d.jpg", id);
    }
}
