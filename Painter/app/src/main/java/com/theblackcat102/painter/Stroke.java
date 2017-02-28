package com.theblackcat102.painter;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by theblackcat on 17/1/17.
 */

public class Stroke {
    private Path _path;
    private Paint _paint;
    public Stroke(Path path, Paint paint){
        _path = path;
        _paint = paint;
    }
    public Paint getPaint(){
        return _paint;
    }
    public Path getPath(){
        return _path;
    }
}
