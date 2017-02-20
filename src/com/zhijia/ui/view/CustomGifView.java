package com.zhijia.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.zhijia.ui.R;

/**
 *
 */
public class CustomGifView extends ImageView {

    private Movie mMovie;
    private long mMovieStart;

    public CustomGifView(Context context) {
        super(context);
        mMovie = Movie.decodeStream(getResources().openRawResource(R.drawable.animation));
    }

    public CustomGifView(Context context, AttributeSet attrs){
        super(context,attrs);
        mMovie = Movie.decodeStream(getResources().openRawResource(R.drawable.animation));
    }

    @Override
    public void onDraw(Canvas canvas){
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) { // first time
            mMovieStart = now;
        }
        if (mMovie != null) {

            int dur = mMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }
            int relTime = (int) ((now - mMovieStart) % dur);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }
}
