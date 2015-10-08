package pacifier.com.app.modules;

import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pacifier.com.app.PaceifierApp;
import pacifier.com.app.managers.GPSSpeedManager;
import pacifier.com.app.managers.TiltSpeedManager;

import static android.content.Context.LOCATION_SERVICE;

@Module(
    includes = {
        NetworkModule.class
    },
    injects = {
        PaceifierApp.class
    },
    library = true
)
public class ApplicationModule {
    final PaceifierApp app;

    public ApplicationModule(PaceifierApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() { return this.app; }

    @Provides @Singleton
    LocationManager provideLocationManager() { return (LocationManager)app.getSystemService(LOCATION_SERVICE); }

    @Provides @Singleton
    SensorManager provideSensorManager() { return (SensorManager)app.getSystemService(Context.SENSOR_SERVICE); }

    @Provides @Singleton
    GPSSpeedManager providerGPSSpeedManager(Application App, LocationManager locationManager) { return new GPSSpeedManager(app, locationManager); }

    @Provides @Singleton
    TiltSpeedManager providerTiltSpeedManager(Application App, SensorManager sensorManager) { return new TiltSpeedManager(app, sensorManager); }
}
