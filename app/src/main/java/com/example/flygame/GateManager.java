package com.example.flygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class GateManager {

    private ArrayList<Gate> gates;
    private int playerGap;
    private int gateGap;
    private int gateHeight;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0 ;

    public GateManager(int playGap, int gateGap, int gateHeight, int color){
        this.playerGap = playGap; //length of gap in gate
        this.gateGap = gateGap; //distance between two gates
        this.gateHeight = gateHeight;
        this.color = color;
        startTime = initTime = System.currentTimeMillis();
        this.gates = new ArrayList<>();
        generateGates();
    }

    public void generateGates(){
        int gapStartY = -Constants.SCREEN_HEIGHT;
        while(gapStartY < 0){
            int gapStartX = (int)(Math.random() * (Constants.SCREEN_WIDTH - playerGap)); //not to be offscreen
            this.gates.add(new Gate(gateHeight, color, gapStartX, gapStartY, playerGap));
            gapStartY += gateHeight + gateGap;
        }
    }

    public boolean collision(RectanglePlayer player){
        for(Gate g : gates) {
            if(g.collision(player))
                return true;
        }
        return  false;
    }

    public void update() {
        if(startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int passedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();

        //how far per second is gate moving
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/5000.0f)) * Constants.SCREEN_HEIGHT/7000.0f;
        for(Gate g : gates){
            g.moveDown(speed * passedTime);// s = v*t
        }
        if (gates.get(gates.size() - 1).getRectangle()[0].top >= Constants.SCREEN_HEIGHT) {
            int gapStartX = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            gates.add(0, new Gate(gateHeight, color, gapStartX, gates.get(0).getRectangle()[0].top - gateHeight - gateGap, playerGap));
            gates.remove(gates.size() - 1);
            score++;
        }
    }

    public void draw(Canvas canvas){
        for(Gate g : gates){
            g.draw(canvas);
        }
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.RED);
        canvas.drawText("" + score, 50, 50 + paint.descent() - paint.ascent(), paint);
    }
}
