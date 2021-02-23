package com.example.flygame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationData implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelSensor;
    private Sensor magnetSensor;
    private float [] accelCurrentData;
    private float [] magnetCurrentData;
    private float [] orientation = new float[3];
    private float [] startOrient = null;

    public  OrientationData(){
        sensorManager = (SensorManager)Constants.CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void register(){
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelCurrentData = event.values;
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magnetCurrentData = event.values;
        if(accelCurrentData != null && magnetCurrentData != null){
            float [] fst = new float[9];
            float [] snd = new float[9];
            boolean rotated = SensorManager.getRotationMatrix(fst, snd, accelCurrentData, magnetCurrentData);//transforming a vector from the device coordinate system to the world's coordinate system
            if(rotated){
                SensorManager.getOrientation(fst, orientation); //computes orientation based od rotation matrix
                if (startOrient == null){
                    startOrient = new float[orientation.length];
                    System.arraycopy(orientation, 0, startOrient, 0, orientation.length);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float [] getOrientation(){
        return orientation;
    }

    public float [] getStartOrient(){
        return startOrient;
    }

    public void newGame() {
        startOrient = null;
    }



}
