package com.zhijia.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * 屏幕工具类
 */
public class ScreenUtil {

    //屏幕的density
    private static float density = 0;

    /**
     * 获得当前屏幕分辨率的宽度像素
     *
     * @param context
     * @return
     */
    public static int getScreenWidthPixels(Context context) {
        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        winManager.getDefaultDisplay().getMetrics(dm);

        return dm.widthPixels;
    }

    /**
     * 获得当前屏幕分辨率的高度像素
     *
     * @param context
     * @return
     */
    public static int getScreenHeightPixels(Context context) {
        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        winManager.getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;
    }

    /**
     * 获得当前屏幕屏幕密度DPI
     *
     * @param context
     * @return
     */
    public static int getScreenDensityDpi(Context context) {
        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        winManager.getDefaultDisplay().getMetrics(dm);

        return dm.densityDpi;
    }

    /**
     * 将DPS转换为像素
     *
     * @param dps     指定的dps
     * @param context
     * @return 转换好的像素近似值（四舍五入）
     */
    public static int dps2pixels(double dps, Context context) {
        if (0 == density) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dps * density + 0.5f);
    }

    /**
     * 将一个View转换成Bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    public static Drawable view2Drawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }
}
