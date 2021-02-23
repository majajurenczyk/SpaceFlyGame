package com.example.flygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class RectanglePlayer implements GameObject {

    private Rect rectangle;
    private int color; //color represented by integer

    public RectanglePlayer(Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;
    }
    public Rect getRectangle(){
        return this.rectangle;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(rectangle, paint); //making rectangle colored
        Drawable d = Constants.CONTEXT.getResources().getDrawable(R.drawable.starship7);
        d.setBounds(rectangle);
        d.draw(canvas);

    }

    @Override
    public void update() {
    }

    public void update(Point point){
        //left, top, right, bottom
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2,
                point.x + rectangle.width()/2, point.y + rectangle.height()/2);
    }
}
