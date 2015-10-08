package pacifier.com.app.events;

public class SensorChangeEvent {
    public final String[] accelerometer;
    public final String[] geomagnetic;
    public final String[] orientation;

    public SensorChangeEvent(String[] accelerometer, String[] geomagnetic, String[] orientation) {
        this.accelerometer = accelerometer;
        this.geomagnetic = geomagnetic;
        this.orientation = orientation;
    }
}
