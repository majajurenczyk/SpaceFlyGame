package com.example.flygame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.MainThread;

public class GameBoard extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread boardThread;
    private RectanglePlayer player;
    private Point playerPoint;
    private GateManager gateManager;

    private boolean playerMoving = false;

    private boolean gameOver = false;
    private long gameOverTime;

    private OrientationData orientData;
    private long frameTime;


    public GameBoard(Context ctx){
        super(ctx);
        getHolder().addCallback(this);
        Constants.CONTEXT = ctx;
        boardThread = new GameThread(getHolder(), this);

        player = new RectanglePlayer(new Rect(200, 200, 300, 300), Color.rgb(0, 0, 255));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        gateManager = new GateManager(400, 450, 80, Color.rgb(169, 169, 169));

        orientData = new OrientationData();
        orientData.register();
        frameTime = System.currentTimeMillis();

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){ //reset of thread
        boardThread = new GameThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        boardThread.setRunning(true); //start game loop
        boardThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while (retry){
            try{
                boardThread.setRunning(false); //stop game loop
                boardThread.join(); //terminate the thread
            }
            catch (Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)event.getY()))
                    playerMoving = true;
                if(gameOver && (System.currentTimeMillis() - gameOverTime) >= 1000) {
                    reset(); //reset our game
                    gameOver = false;
                    orientData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(playerMoving && !gameOver)
                    playerPoint.set((int)event.getX(), (int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                playerMoving = false;
                break;
        }
        return true;
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        gateManager = new GateManager(400, 450, 80, Color.WHITE);
        playerMoving = false;
    }

    public void update() { //update game frame by frame
        if(!gameOver) {
            if(frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            long passedTime = System.currentTimeMillis() - frameTime;
            frameTime = System.currentTimeMillis();

            if(orientData.getOrientation() != null && orientData.getStartOrient() != null) {
                float y_change = orientData.getOrientation()[1] - orientData.getStartOrient()[1]; //delta
                float x_change = orientData.getOrientation()[2] - orientData.getStartOrient()[2];

                float speedX = 2*x_change * Constants.SCREEN_WIDTH/800f; // V = s/t
                float speedY = y_change * Constants.SCREEN_HEIGHT/800f;

                playerPoint.x += (int)(speedX*passedTime); // s = v*t
                playerPoint.y -= (int)(speedY*passedTime);
            }

            if(playerPoint.x < 0){ //stop at the edge of the screen
                playerPoint.x = 0;
            }
            else if (playerPoint.x > Constants.SCREEN_WIDTH){
                playerPoint.x = Constants.SCREEN_WIDTH;
            }
            if(playerPoint.y < 0){
                playerPoint.y = 0;
            }
            else if (playerPoint.y > Constants.SCREEN_HEIGHT){
                playerPoint.y = Constants.SCREEN_HEIGHT;
            }

            player.update(playerPoint);
            gateManager.update();
            if(gateManager.collision((player))){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }

    }

    @Override
    public void draw(Canvas canvas) { //take canvas and draw a game

        super.draw(canvas);
        /*@SuppressLint("UseCompatLoadingForDrawables") Drawable img = getContext().getResources().getDrawable(R.drawable.stars1);
        Rect imgB = canvas.getClipBounds();

        img.setBounds(imgB);
        img.draw(canvas);*/


        canvas.drawColor(Color.BLACK);
        player.draw(canvas);
        gateManager.draw(canvas);



        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.RED);
            drawText(canvas, paint, "Game over");
        }
    }

    private Rect r = new Rect();

    private void drawText(Canvas canvas, Paint paint, String text) { //stack
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}

