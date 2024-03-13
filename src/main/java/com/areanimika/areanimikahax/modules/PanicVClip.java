package com.areanimika.areanimikahax.modules;

import com.areanimika.areanimikahax.Addon;
import com.areanimika.areanimikahax.utils.MovementUtils;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.movement.Flight;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

public class PanicVClip extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> blocks = sgGeneral.add(new DoubleSetting.Builder()
            .name("blocks")
            .min(0)
            .defaultValue(100)
            .build()
    );

    private final Setting<Boolean> alsoFly = sgGeneral.add(new BoolSetting.Builder()
            .name("also-fly")
            .description("Also enable Flight on panic")
            .defaultValue(true)
            .build()
    );

    public PanicVClip() {
        super(Addon.CATEGORY, "panic-vclip", "Teleports you vertically up a set amount of blocks when you pop a totem.");
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!(event.packet instanceof EntityStatusS2CPacket p)) return;
        if (p.getStatus() != 35) return;

        Entity entity = p.getEntity(mc.world);
        if (entity == null || !(entity.equals(mc.player))) return;

        MovementUtils.saferVClip(MovementUtils.findHighestPossibleTeleportationDifference(mc.player.getY(), blocks.get()));

        if(alsoFly.get()) {
            Flight flight =  Modules.get().get(Flight.class);
            if(!flight.isActive()) flight.toggle();
        }
    }
}
