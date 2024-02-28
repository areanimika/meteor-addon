package com.areanimika.areanimikahax.event;

import net.minecraft.entity.LivingEntity;

public class LivingDeathEvent {
    private static final LivingDeathEvent INSTANCE = new LivingDeathEvent();

    public LivingEntity entity;

    public static LivingDeathEvent get(LivingEntity entity) {
        INSTANCE.entity = entity;
        return INSTANCE;
    }
}
