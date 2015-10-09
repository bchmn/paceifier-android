package pacifier.com.app.events;

public class SpeedChangeEvent {
    public final float speed;
    public final int speedLimit;

    public SpeedChangeEvent(float speed, int speedLimit) {
        this.speed = speed;
        this.speedLimit = speedLimit;
    }
}
