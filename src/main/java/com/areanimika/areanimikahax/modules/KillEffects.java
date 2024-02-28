package com.areanimika.areanimikahax.modules;

import com.areanimika.areanimikahax.Addon;
import com.areanimika.areanimikahax.event.CameraOffsetEvent;
import com.areanimika.areanimikahax.event.LivingDeathEvent;
import com.areanimika.areanimikahax.utils.MathUtils;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EntityTypeListSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class KillEffects extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Set<EntityType<?>>> entities = sgGeneral.add(new EntityTypeListSetting.Builder()
            .name("entities")
            .description("Death of which entities will play the effect.")
            .onlyAttackable()
            .defaultValue(EntityType.PLAYER)
            .build()
    );

    private final Setting<Double> caresRange = sgGeneral.add(new DoubleSetting.Builder()
            .name("cares-range")
            .description("Radius in which entity deaths count")
            .min(0)
            .defaultValue(32)
            .build()
    );

    private final Setting<Boolean> includeSelf = sgGeneral.add(new BoolSetting.Builder()
            .name("include-self")
            .description("Should an effect play when -you- die.")
            .defaultValue(false)
            .build()
    );

    /* Flash */
    private final SettingGroup sgFlash = settings.createGroup("Flash");

    private final Setting<Boolean> flashEnabled = sgFlash.add(new BoolSetting.Builder()
            .name("flash-enabled")
            .description("Flashes your screen when an entity dies.")
            .defaultValue(false)
            .build()
    );

    private final Setting<SettingColor> flashColor = sgFlash.add(new ColorSetting.Builder()
            .name("flash-color")
            .description("Color of the screen flash. Alpha will be ignored.")
            .defaultValue(Color.WHITE)
            .visible(flashEnabled::get)
            .build()
    );

    private final Setting<Double> flashDurationSeconds = sgFlash.add(new DoubleSetting.Builder()
            .name("flash-duration")
            .description("For how long will the screen flash (Fade out time)")
            .min(0)
            .defaultValue(1.5)
            .visible(flashEnabled::get)
            .build()
    );

    private final Setting<Integer> maxAlpha = sgFlash.add(new IntSetting.Builder()
            .name("flash-max-alpha")
            .description("Max alpha for the flash.")
            .min(0)
            .max(255)
            .sliderRange(0, 255)
            .visible(flashEnabled::get)
            .build()
    );

    /* Screen shake */
    private final SettingGroup sgShake = settings.createGroup("Shake");

    private final Setting<Boolean> shakeEnabled = sgShake.add(new BoolSetting.Builder()
            .name("shake-enabled")
            .description("Shakes your screen when an entity dies.")
            .defaultValue(false)
            .build()
    );

    private final Setting<Double> shakePower = sgShake.add(new DoubleSetting.Builder()
            .name("shake-power")
            .defaultValue(0.07)
            .min(0)
            .visible(shakeEnabled::get)
            .build()
    );

    private final Setting<Double> shakeDuration = sgShake.add(new DoubleSetting.Builder()
            .name("shake-duration")
            .defaultValue(0.2)
            .min(0)
            .visible(shakeEnabled::get)
            .build()
    );

    /* Other Basic Effects */
    private final SettingGroup sgEffects = settings.createGroup("Basic Effects");

    private final Setting<Boolean> effectLightning = sgEffects.add(new BoolSetting.Builder()
            .name("effect-lightning")
            .description("Strikes a lightning.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> effectBlood = sgEffects.add(new BoolSetting.Builder()
            .name("effect-blood")
            .description("A blood explosion.")
            .defaultValue(true)
            .build()
    );

    /* Data */

    private double flashDurationLeft = 0;
    private Long lastRenderMs = null;

    private double shakeLeft = 0;

    public KillEffects() {
        super(Addon.CATEGORY, "kill-effects", "Effects when you kill someone. Feature requested by @Umnyasha");
    }

    @EventHandler
    public void onEntityDied(LivingDeathEvent event) {
        LivingEntity entity = event.entity;

        if(!entities.get().contains(entity.getType())) return;
        if(entity.distanceTo(mc.player) > caresRange.get()) return;
        if(!includeSelf.get() && entity == mc.player) return;

        if(flashEnabled.get()) flashDurationLeft = flashDurationSeconds.get();

        if(shakeEnabled.get()) shakeLeft = shakeDuration.get();

        if(effectLightning.get()) {
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, mc.world);
            lightning.setPosition(entity.getPos());

            mc.world.addEntity(lightning);
        }

        if(effectBlood.get()) {
            mc.world.addBlockBreakParticles(entity.getBlockPos(), Blocks.RED_WOOL.getDefaultState());
            mc.world.addBlockBreakParticles(entity.getBlockPos(), Blocks.REDSTONE_BLOCK.getDefaultState());
            mc.world.addBlockBreakParticles(entity.getBlockPos(), Blocks.NETHER_WART_BLOCK.getDefaultState());
        }
    }

    @EventHandler
    public void onRender2D(Render2DEvent event) {
        if(lastRenderMs == null) {
            lastRenderMs = System.currentTimeMillis();
            return;
        }

        double deltaTime = (System.currentTimeMillis() - lastRenderMs) / 1000D;
        lastRenderMs = System.currentTimeMillis();

        if(flashDurationLeft > 0 && flashEnabled.get()) {
            flashDurationLeft = Math.max(flashDurationLeft - deltaTime, 0);
            double p = flashDurationLeft / flashDurationSeconds.get();

            Color color = flashColor.get().copy().a((int) (p * maxAlpha.get()));

            event.drawContext.fill(0, 0, event.screenWidth * 2, event.screenHeight * 2, color.getPacked());
        }
    }

    @EventHandler
    public void onCameraOffset(CameraOffsetEvent event) {
        if(!shakeEnabled.get()) shakeLeft = 0;

        if(shakeLeft > 0) {
            double deltaTime = (System.currentTimeMillis() - lastRenderMs) / 1000D;
            shakeLeft = Math.max(shakeLeft - deltaTime, 0);

            double shakeLeftProgress = shakeLeft / shakeDuration.get();
            Vec3d vec = MathUtils.randomSphereVector().multiply(shakePower.get() * shakeLeftProgress);
            event.x = vec.x;
            event.y = vec.y;
            event.z = vec.z;
        }
    }
}
