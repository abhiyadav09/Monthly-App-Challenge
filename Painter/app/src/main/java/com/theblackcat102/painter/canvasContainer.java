package com.theblackcat102.painter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theblackcat on 15/1/17.
 */

public class canvasContainer extends View {
    public int height;
    public int width;
    private Canvas canvas;
    private List<Stroke> strokes;
    private Bitmap bitmap;
    Context context;
    private static final float TOLERANCE = 5;
    private float mX, mY;
    private float brushSize;
    private int brushColor;

    public canvasContainer(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        strokes = new ArrayList<Stroke>();
        brushSize = 8f;
        brushColor = Color.BLACK;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Stroke s : strokes) {
            canvas.drawPath(s.getPath(), s.getPaint());
        }
    }

    private void startTouch(float x, float y) {
        Path mPath;
        Paint mPaint;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(brushColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(brushSize);
        mPath.moveTo(x, y);
        strokes.add(new Stroke(mPath, mPaint));
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y) {
        Path drawPath = strokes.get(strokes.size() - 1).getPath();
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            drawPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void upTouch() {
        Path path = strokes.get(strokes.size() - 1).getPath();
        path.lineTo(mX, mY);
    }

    public void clearCanvas() {
        strokes.clear();
        invalidate();
    }

    public void undoCanvas() {
        strokes.remove(strokes.size()-1);
        invalidate();
    }


    public void changeBrushColor(int color){
        Path mPath;
        Paint mPaint;
        brushColor = color;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(brushColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(brushSize);
        mPath.moveTo(mX,mY);
        strokes.add(new Stroke(mPath,mPaint));
    }
    public void changeBrushSize(float size){
        Path mPath;
        Paint mPaint;
        brushSize = 20f*size+8f;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(brushColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(brushSize);
        mPath.moveTo(mX,mY);
        strokes.add(new Stroke(mPath,mPaint));
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public Canvas getCanvas(){
        for(Stroke s: strokes){
            canvas.drawPath(s.getPath(),s.getPaint());
        }
        return canvas;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}
