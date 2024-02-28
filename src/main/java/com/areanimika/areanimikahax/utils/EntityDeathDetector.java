package com.areanimika.areanimikahax.utils;

import com.areanimika.areanimikahax.event.LivingDeathEvent;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class EntityDeathDetector {
    private static final EntityDeathDetector INSTANCE = new EntityDeathDetector();
    public static void init() {
        MeteorClient.EVENT_BUS.subscribe(INSTANCE);
    }

    private final HashMap<UUID, Boolean> lastSeenStates = new HashMap<>();

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if(!Utils.canUpdate()) {
            lastSeenStates.clear();
            return;
        }

        Set<UUID> seen = new HashSet<>();

        for (Entity entity : mc.world.getEntities()) {
            if(!(entity instanceof LivingEntity living)) continue;

            UUID uuid = living.getUuid();
            seen.add(uuid);

            if(lastSeenStates.get(uuid) != null && lastSeenStates.get(uuid) != living.isDead() && living.isDead()) {
                MeteorClient.EVENT_BUS.post(LivingDeathEvent.get(living));
            }

            lastSeenStates.put(uuid, living.isDead());
        }

        lastSeenStates.keySet().removeIf(it -> !seen.contains(it));
    }
}
