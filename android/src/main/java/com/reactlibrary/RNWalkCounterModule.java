
package com.reactlibrary;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import static android.content.Context.SENSOR_SERVICE;

public class RNWalkCounterModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private int steps = 0;
  double bearingRadian = 0;
  SensorManager sensorManager;
  Sensor accSensor;
  Sensor rotSensor;
  SensorEventListener stepListener,orientationListener;

  public RNWalkCounterModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNWalkCounter";
  }

  @ReactMethod
  public void startCounter(){
    Toast.makeText(getReactApplicationContext(),"Step Started",Toast.LENGTH_LONG).show();
    runStepCounter();
    this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("onStepStart",null);

  }

  public void runStepCounter(){
    sensorManager = (SensorManager) this.getCurrentActivity().getSystemService(SENSOR_SERVICE);
    accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    rotSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    stepListener = new StepSensorEventListener();
    orientationListener = new OrientationSensorEventListener();

    sensorManager.registerListener(stepListener, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
    sensorManager.registerListener(orientationListener, rotSensor, SensorManager.SENSOR_DELAY_NORMAL);
  }

  public void onStepRunning(){
    this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("onStepRunning",steps);
  }

  @ReactMethod
  public void stopCounter(){
    sensorManager.unregisterListener(stepListener);
    sensorManager.unregisterListener(orientationListener);
    steps=0;
  }


  class StepSensorEventListener implements SensorEventListener {

    protected Context mContext;

//    public StepSensorEventListener(Context context){
//      mContext = context;
//    }

    public void onAccuracyChanged(Sensor s, int i){}

    public void onSensorChanged(SensorEvent se){
      Log.e("onSensorChanged",""+se);
      Toast.makeText(getReactApplicationContext(),""+se,Toast.LENGTH_LONG).show();
      if(se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
        accValues.addPoint(se.values[2]); // Give values to be averaged to the processing class.
      }

      float[] avgValuesZ = {(float)0.0, (float)0.0, accValues.getAvgPointZ()};
      //graph.addPoint(avgValuesZ); // Graph the average values.

      // Count a new step if certain algorithm conditions are met.
      if (accValues.sign == -2 && accValues.state == 1 && accValues.stepCheckEnabled){
        accValues.state = 0;
        accValues.stepCount++;
        // Ensures sin and cos calculate from the same angle.
        double tempBearing = bearingRadian;
        // North and East components of each step are the cos and sin of the bearing when the step is taken, respectively.
        accValues.stepCountNorth += Math.cos(tempBearing);
        accValues.stepCountEast += Math.sin(tempBearing);
      }

      steps+=accValues.stepCount;
      onStepRunning();

      //output.setText(String.format("Steps: %d%n", accValues.stepCount));
    }
  }

  class OrientationSensorEventListener implements SensorEventListener{
    //TextView output;
    float [] rotation = new float[16];
    float [] orientation = new float[3];
    double bearingDegree = 0;

//    public OrientationSensorEventListener(TextView input){
//      output = input;
//    }

    public void onAccuracyChanged(Sensor s, int i){}

    public void onSensorChanged(SensorEvent se){
      if(se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
        SensorManager.getRotationMatrixFromVector(rotation , se.values);
        SensorManager.getOrientation(rotation, orientation);
      }

      SensorManager.getOrientation(rotation, orientation);
      bearingRadian = orientation[0];
      bearingDegree = Math.toDegrees(bearingRadian);

      // If the bearing is negative, add 360 to make sure the bearing is always positive.
      if(bearingDegree < 0) {
        bearingDegree += 360;
      }

     // output.setText(String.format("Bearing: %.3f degrees%nSteps North: %.3f steps%nSteps East: %.3f steps%n", bearingDegree, accValues.stepCountNorth, accValues.stepCountEast));
    }
  }

}