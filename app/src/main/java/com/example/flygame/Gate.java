package com.example.flygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class Gate implements GameObject{
    private Rect fst_rectangle;
    private Rect snd_rectangle;
    private int color; //color represented by integer;

    public Gate(int rectHigh, int color, int xGapStart, int yGapStart, int gap){
        this.color = color;
        fst_rectangle = new Rect(0, yGapStart, xGapStart, yGapStart + rectHigh);
        snd_rectangle = new Rect(xGapStart + gap, yGapStart, Constants.SCREEN_WIDTH, yGapStart + rectHigh);
    }
    public Rect [] getRectangle(){
        return new Rect[]{fst_rectangle, snd_rectangle};
    }

    public void moveDown(float y){ //increment Y
        fst_rectangle.top += y;
        fst_rectangle.bottom += y;
        snd_rectangle.top += y;
        snd_rectangle.bottom += y;
    }

    public boolean collision(RectanglePlayer player){
        return Rect.intersects(fst_rectangle, player.getRectangle()) || Rect.intersects(snd_rectangle, player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        Drawable img1 = Constants.CONTEXT.getResources().getDrawable(R.drawable.stone1);
        Drawable img2 = Constants.CONTEXT.getResources().getDrawable(R.drawable.stone1);

        img1.setBounds(fst_rectangle);
        img1.draw(canvas);
        img2.setBounds(snd_rectangle);
        img2.draw(canvas);
        //paint.setColor(color);
        //canvas.drawRect(fst_rectangle, paint);
        //canvas.drawRect(snd_rectangle, paint);
    }

    @Override
    public void update() {

    }
}
