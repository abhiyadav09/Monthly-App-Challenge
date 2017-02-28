package com.theblackcat102.painter;

import java.text.DateFormat;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends Activity {
    private canvasContainer customCanvas;
    private SeekBar brushResizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customCanvas = (canvasContainer) findViewById(R.id.signature_canvas);
        brushResizer = (SeekBar) findViewById(R.id.sizeSelector);
        brushResizer.setVisibility(View.GONE);


        FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                customCanvas.clearCanvas();
            }
        });

        //dynamic adding floating button
        final FloatingActionButton actionC = (FloatingActionButton) findViewById(R.id.action_resize);
        actionC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brushResizer.getVisibility() == View.GONE && actionC.getVisibility() == View.VISIBLE){
                    brushResizer.setVisibility(View.VISIBLE);
                    actionC.setTitle("");
                }
                else{
                    brushResizer.setVisibility(View.GONE);
                    actionC.setTitle("Brush Size");
                }
            }
        });

        final FloatingActionButton shareAction = (FloatingActionButton) findViewById(R.id.share_action);
        shareAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                customCanvas.setDrawingCacheEnabled(true);
                Bitmap picture = customCanvas.getDrawingCache();
                //Canvas c = customCanvas.getCanvas();

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/png");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                picture.compress(Bitmap.CompressFormat.PNG,100,bytes);
                final String imagename = UUID.randomUUID().toString() + ".png";

                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                // Create imageDir
                File mypath =new File(directory,imagename);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    picture.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //               MediaStore.Images.Media.insertImage(getContentResolver(), customCanvas.getDrawingCache(), imagename, "drawing");
               /*
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("fileName"));
                startActivity(Intent.createChooser(share,"Share Image"));*/
            }
        });

        brushResizer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                brushResizer.setVisibility(View.GONE);
                actionC.setTitle("Brush Size");
            }

            @Override
            public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser){
                float percentage = ((float)progress/(float)100.0);
                customCanvas.changeBrushSize(percentage);
            }

        });
    }
    public void changeBlack(View v){
        customCanvas.changeBrushColor(Color.parseColor("#B2000000"));
    }

    public void changeBlue(View v){

        customCanvas.changeBrushColor(Color.parseColor("#B23F51B5"));
    }

    public void changeRed(View v){

        customCanvas.changeBrushColor(Color.parseColor("#B2F73859"));
    }

    public void changeRedPink(View v){
        customCanvas.changeBrushColor(Color.parseColor("#B2ED5485"));
    }

    public void changeYellow(View v){
        customCanvas.changeBrushColor(Color.parseColor("#B2FDDA16"));
    }

    public void changeOrange(View v){
        customCanvas.changeBrushColor(Color.parseColor("#B2FF7844"));
    }


}