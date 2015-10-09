package pacifier.com.app.managers;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Arrays;

import de.greenrobot.event.EventBus;
import pacifier.com.app.events.AccelerationChangeEvent;
import pacifier.com.app.events.SensorChangeEvent;
import pacifier.com.app.events.SpeedChangeEvent;
import pacifier.com.app.utils.Logger;

public class TiltSpeedManager implements SensorEventListener{
    final Application mApp;
    final SensorManager mSensorManager;
    final Sensor mAccelerometerSensor;
    final Sensor mMagnometerSensor;
    boolean isListening = false;
    float[] mGravity;
    float[] mPreviousGravity;
    float[] mGeomagnetic;
    float[] mGeomagneticBaseline;
    Float mLastSpeed;



    String[] mAccelerometer =  new String[3];
    String[] mMagnetic =  new String[3];
    String[] mOrientationString =  new String[3];
    float[] mOrientation = new float[3];
    float[] mRotationM = new float[16];
    float[] mInclinationM = new float[16];

    public TiltSpeedManager(Application app, SensorManager sensorManager) {
        mApp = app;
        mSensorManager = sensorManager;
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void startListening() {
        if (!isListening) {
            isListening = true;
            mGeomagneticBaseline = null;
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mMagnometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stopListening() {
        if (isListening) {
            mSensorManager.unregisterListener(this);
            isListening = false;
        }
    }

    public void resetBaseline() {
        if (null != mGeomagnetic)
            mGeomagneticBaseline = Arrays.copyOf(mGeomagnetic, mGeomagnetic.length);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                mGravity = sensorEvent.values;
                if (null != mPreviousGravity) {
                    float smoothAcceleration = (float)((mPreviousGravity[0] * 0.9) + (mGravity[0] * 0.1));
                    EventBus.getDefault().post(new AccelerationChangeEvent(smoothAcceleration));
                }
            }
            else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            {
                mGeomagnetic = sensorEvent.values;
                if (null == mGeomagneticBaseline) {
                    mGeomagneticBaseline = Arrays.copyOf(mGeomagnetic, mGeomagnetic.length);
                }
            }

            if (null != mGeomagneticBaseline && null != mGeomagnetic) {
                float diff = Math.abs(mGeomagneticBaseline[0] - mGeomagnetic[0]);
                float speedF = diff * 3;
                //float smooth_speed = (null != mLastSpeed) ? (float)((mLastSpeed * 0.8) + (speedF * 0.2)) : speedF;
                int speed = Math.round(speedF);
                mLastSpeed = speedF;
                Logger.l("Speed: " + Integer.toString(speed) + " km/h");
                EventBus.getDefault().post(new SpeedChangeEvent(speed, 90));
            }

            if (null != mGravity && null != mGeomagnetic) {
                boolean success = SensorManager.getRotationMatrix(mRotationM, mInclinationM, mGravity, mGeomagnetic);
                if (success) {
                    SensorManager.getOrientation(mRotationM, mOrientation);
                    for(int i=0; i <= 2; i++) {
                        mAccelerometer[i] = Float.toString(mGravity[i]);
                        mMagnetic[i] = Float.toString(mGeomagnetic[i]);
                        mOrientationString[i] = Float.toString(mOrientation[i]);
                    }
                }
                EventBus.getDefault().post(new SensorChangeEvent(mAccelerometer, mMagnetic, mOrientationString));
            }

            if (null != mGravity)
                mPreviousGravity = Arrays.copyOf(mGravity, mGravity.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
