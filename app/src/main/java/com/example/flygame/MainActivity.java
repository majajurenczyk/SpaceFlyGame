package com.example.flygame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make game fullscreen, get rid of toolbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dis_met = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dis_met);

        Constants.SCREEN_WIDTH = dis_met.widthPixels;
        Constants.SCREEN_HEIGHT = dis_met.heightPixels;
        setContentView(new GameBoard(this));
    }
}