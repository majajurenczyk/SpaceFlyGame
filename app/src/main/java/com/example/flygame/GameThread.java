package com.example.flygame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends  Thread {
    public static final int MAX_FPS = 500;
    private SurfaceHolder surfHolder;
    private GameBoard gameBoard;
    private boolean if_running;
    public static Canvas canvas; //what we draw

    public GameThread(SurfaceHolder surfHolder, GameBoard gameBoard){
        super();
        this.surfHolder = surfHolder;
        this.gameBoard = gameBoard;
    }

    public void setRunning(boolean running){
        this.if_running = running;
    }

    @Override
    public void run(){ //run from Thread
        long startTime; //poczatek petli
        long timeMill = 1000/MAX_FPS; //ile zajela klatka
        long waitTime; //czas ile musi poczekac program aby zaktualizowac kolejna klatke

        int frameCount = 0; //liczba klatek
        long allTime = 0;
        long targetTime = 1000/MAX_FPS; //ile powinna zajac klatka

        while(if_running){
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfHolder.lockCanvas();
                synchronized (surfHolder){
                    this.gameBoard.update(); //inform to move, change the next values stored by game
                    this.gameBoard.draw(canvas); //draw on screen what was updated
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            finally {
                if(canvas != null){
                    try{
                        surfHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            timeMill = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMill;
            try{
                if(waitTime > 0){ //finish the frame earlier than the target time
                    sleep(waitTime);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            allTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS){
                double avgFPS = (int) (1000 / ((allTime / frameCount) / 1000000));
                frameCount = 0;
                allTime = 0;
                //System.out.println(avgFPS);
            }
        }
    }
}
