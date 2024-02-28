package com.areanimika.areanimikahax.event;

public class CameraOffsetEvent {
    private static final CameraOffsetEvent INSTANCE = new CameraOffsetEvent();

    public double x;
    public double y;
    public double z;

    public static CameraOffsetEvent get() {
        INSTANCE.x = 0;
        INSTANCE.y = 0;
        INSTANCE.z = 0;

        return INSTANCE;
    }
}
