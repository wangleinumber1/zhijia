package com.zhijia.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 下载图片,暂时没有考虑缓存
 */
public class DownloadImageTask {


    /**
     * 异步加载图片，图片可能是缓存
     *
     * @param url                    图片URL
     * @param imageView              要显示图片的ImageView
     * @param defaultImageResourceId 如果加载失败，默认的图片。
     * @return
     */
    public void doInBackground(String url, final ImageView imageView, final Integer defaultImageResourceId) {
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(url, imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (defaultImageResourceId != null) {
                    imageView.setImageResource(defaultImageResourceId);
                }
                String message = null;  
                switch (failReason.getType()) { // 获取图片失败类型  
                    case IO_ERROR:              // 文件I/O错误  
                        message = "Input/Output error";  
                        break;  
                    case DECODING_ERROR:        // 解码错误  
                        message = "Image can't be decoded";  
                        break;  
                    case NETWORK_DENIED:        // 网络延迟  
                        message = "Downloads are denied";  
                        break;  
                    case OUT_OF_MEMORY:         // 内存不足  
                        message = "Out Of Memory error";  
                        break;  
                    case UNKNOWN:               // 原因不明  
                        message = "Unknown error";  
                        break;  
                }  
                System.out.println("获取图片失败的原因:"+message);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }
        });
    }

    /**
     * 异步加载图片，图片可能是缓存
     *
     * @param url                    图片URL
     * @param imageView              要显示图片的ImageView
     * @param defaultImageResourceId 如果加载失败，默认的图片。
     * @param isGone                 是否让imageView gone
     * @return
     */
    public void doInBackground(String url, final ImageView imageView, final Integer defaultImageResourceId, final boolean isGone) {
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(url, imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (defaultImageResourceId != null) {
                    imageView.setImageResource(defaultImageResourceId);
                    
                }

                if (isGone) {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }
        });
    }

    /**
     * 异步加载图片，图片可能是缓存
     *
     * @param url                    图片URL
     * @param imageView              要显示图片的ImageView
     * @param defaultImageResourceId 如果加载失败，默认的图片。
     * @param isGone                 是否让imageView gone
     * @return
     */
    public void doInBackground(String url, final ImageView imageView, final Integer defaultImageResourceId, final boolean isGone, final View.OnClickListener clickListener) {
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(url, imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (defaultImageResourceId != null) {
                    imageView.setImageResource(defaultImageResourceId);
                }

                if (isGone) {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (clickListener != null) {
                    view.setOnClickListener(clickListener);
                }
            }
        });
    }
}
