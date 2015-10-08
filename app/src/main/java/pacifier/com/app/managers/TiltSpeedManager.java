package pacifier.com.app.managers;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Arrays;

import de.greenrobot.event.EventBus;
import pacifier.com.app.events.SensorChangeEvent;
import pacifier.com.app.utils.Logger;

public class TiltSpeedManager implements SensorEventListener{
    final Application mApp;
    final SensorManager mSensorManager;
    final Sensor mAccelerometerSensor;
    final Sensor mMagnometerSensor;
    boolean isListening = false;
    long mCurrentPollTimeStamp;


    float[] mGravity;
    float[] mGeomagnetic;
    float[] mGeomagneticBaseline;
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            mCurrentPollTimeStamp = sensorEvent.timestamp;

            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                mGravity = sensorEvent.values;
            }
            else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            {
                mGeomagnetic = sensorEvent.values;
                if (null == mGeomagneticBaseline)
                    mGeomagneticBaseline = Arrays.copyOf(mGeomagnetic, mGeomagnetic.length);
            }

            if (null != mGeomagneticBaseline && null != mGeomagnetic) {
                float diff = Math.abs(mGeomagneticBaseline[1] - mGeomagnetic[1]);
                float speed = diff * 5;
                Logger.l("Speed: " + Float.toString(speed) + " km/h");
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
                EventBus.getDefault().post(new SensorChangeEvent(mAccelerometer, mMagnetic, mOrientationString) );
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
