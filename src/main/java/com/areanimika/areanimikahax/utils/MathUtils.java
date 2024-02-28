package com.areanimika.areanimikahax.utils;

import net.minecraft.util.math.Vec3d;

public class MathUtils {
    public static Vec3d randomSphereVector() {
        double y = Math.random() * 360;
        double p = Math.random() * 360;

        return yawPitchToVec(y, p).normalize();
    }

    public static Vec3d yawPitchToVec(double yaw, double pitch) {
        double x = Math.cos(yaw) * Math.cos(pitch);
        double y = Math.sin(yaw) * Math.cos(pitch);
        double z = Math.sin(pitch);

        return new Vec3d(x, y, z);
    }
}
