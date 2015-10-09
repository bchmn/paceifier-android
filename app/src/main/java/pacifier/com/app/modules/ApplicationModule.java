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
import pacifier.com.app.utils.Logger;

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
    PaceifierApp provideApplication() { return this.app; }

    @Provides @Singleton
    LocationManager provideLocationManager() { return (LocationManager)app.getSystemService(LOCATION_SERVICE); }

    @Provides @Singleton
    SensorManager provideSensorManager() { return (SensorManager)app.getSystemService(Context.SENSOR_SERVICE); }

    @Provides @Singleton
    GPSSpeedManager providerGPSSpeedManager(PaceifierApp App, LocationManager locationManager) { return new GPSSpeedManager(app, locationManager); }

    @Provides @Singleton
    TiltSpeedManager providerTiltSpeedManager(PaceifierApp App, SensorManager sensorManager) { return new TiltSpeedManager(app, sensorManager); }
}
