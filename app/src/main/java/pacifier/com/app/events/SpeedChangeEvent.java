package pacifier.com.app.events;

public class SpeedChangeEvent {
    public final int speed;
    public final int speedLimit;

    public SpeedChangeEvent(int speed, int speedLimit) {
        this.speed = speed;
        this.speedLimit = speedLimit;
    }
}
