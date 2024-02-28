package com.areanimika.areanimikahax.modules;

import com.areanimika.areanimikahax.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixin.PlayerMoveC2SPacketAccessor;
import meteordevelopment.meteorclient.mixininterface.IPlayerMoveC2SPacket;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AlwaysNoFallPacket extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> oldModeWhenElytraFlying = sgGeneral.add(new BoolSetting.Builder()
            .name("disable-when-elytra")
            .description("Fixes issues with elytra.")
            .defaultValue(true)
            .build()
    );

    public AlwaysNoFallPacket() {
        super(Addon.CATEGORY, "packet-always-no-fall", "Should be better then normal No Fall when using v-clip.");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if(!(event.packet instanceof PlayerMoveC2SPacket)) return;
        if(((IPlayerMoveC2SPacket) event.packet).getTag() == 1337) return;
        if(mc.player.getAbilities().creativeMode) return;
        if(oldModeWhenElytraFlying.get() && mc.player.isFallFlying()) return;

        PlayerMoveC2SPacketAccessor p = (PlayerMoveC2SPacketAccessor) event.packet;
        p.setOnGround(true);
    }
}
