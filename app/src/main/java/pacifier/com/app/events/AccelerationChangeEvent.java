package pacifier.com.app.events;

public class AccelerationChangeEvent {
    private final float acceleration;

    public AccelerationChangeEvent(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getAcceleration() {
        return this.acceleration;
    }
}
