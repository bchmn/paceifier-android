package pacifier.com.app.managers;

import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import de.greenrobot.event.EventBus;
import pacifier.com.app.events.SpeedChangeEvent;

public class GPSSpeedManager implements LocationListener {
    private final Application mApp;
    private final LocationManager mLocationManager;
    private boolean isListening = false;

    public GPSSpeedManager(Application app, LocationManager locationManager) {
        mApp = app;
        mLocationManager = locationManager;
    }

    public void startListening() {
        if (!isListening) {
            if (mApp.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                isListening = true;
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    public void stopListening() {
        if (isListening) {
            if (mApp.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.removeUpdates(this);
                isListening = false;
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        EventBus.getDefault().post(new SpeedChangeEvent(location.getSpeed()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
