package com.vcredit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhaoguangyou on 2016/3/18.
 */

/**
 * This view implements the drawing canvas.
 * <p>
 * It handles all of the input events and drawing functions.
 */
public class HandWriteView extends View {
    private Paint paint;
    private Canvas cacheCanvas;
    private Bitmap cachebBitmap;
    private Path path;
    private int BACKGROUND_COLOR = Color.WHITE;
    private int bitmapHeight = 0;
    private int bitmapWidth =0;

    public Bitmap getCachebBitmap() {
        return cachebBitmap;
    }
    public  HandWriteView(Context context) {
        super(context);
        init();
    }
    public HandWriteView(Context context, int bimapheight,int bitmapWidth) {
        super(context);
        this.bitmapHeight = bimapheight;
        this.bitmapWidth =bitmapWidth;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        path = new Path();

        //1.创建跟图片基本一样大小的白纸（排除四个角的间隙）
        cachebBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
//        cacheCanvas.clipRect(0, marginTop, width, bitmapHeight+marginTop);//控制画板的区域坐标(x,y,x+width,y+high);
        cacheCanvas.drawColor(Color.WHITE);

    }

    public void clear() {
        if (cacheCanvas != null) {

            paint.setColor(BACKGROUND_COLOR);
            cacheCanvas.drawPaint(paint);
            paint.setColor(Color.BLACK);
            cacheCanvas.drawColor(Color.WHITE);
            invalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(cachebBitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
        int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
        if (curW >= w && curH >= h) {
            return;
        }

        if (curW < w)
            curW = w;
        if (curH < h)
            curH = h;

        Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);
        if (cachebBitmap != null) {
            newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
        }
        cachebBitmap = newBitmap;
        cacheCanvas = newCanvas;
    }

    private float cur_x, cur_y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                cur_x = x;
                cur_y = y;
                path.moveTo(cur_x, cur_y);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                path.quadTo(cur_x, cur_y, x, y);
                cur_x = x;
                cur_y = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
            }
        }
        invalidate();
        return true;
    }
}
