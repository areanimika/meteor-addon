package com.areanimika.areanimikahax.mixin;

import com.areanimika.areanimikahax.mixinterface.ILightningEntity;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.entity.LightningEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin implements ILightningEntity {
    @Unique private Color color = null;
    @Unique private double thunderVolume = 10000;
    @Unique private double impactVolume = 2;
    @Unique private SoundCategory soundCategory = SoundCategory.WEATHER;

    @Override
    public Color ama$getColorOverride() {
        return color;
    }

    @Override
    public void ama$setColor(Color color) {
        this.color = color;
    }

    @Override
    public double ama$getThunderVolume() {
        return thunderVolume;
    }

    @Override
    public void ama$setThunderVolume(double volume) {
        this.thunderVolume = volume;
    }

    @Override
    public double ama$getImpactVolume() {
        return impactVolume;
    }

    @Override
    public void ama$setImpactVolume(double volume) {
        this.impactVolume = volume;
    }

    @Override public SoundCategory ama$getSoundCategory() {
        return soundCategory;
    }

    @Override
    public void ama$setSoundCategory(SoundCategory soundCategory) {
        this.soundCategory = soundCategory;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"), method = "tick")
    public void onPlaySound(World instance, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
        double playVolume = volume;
        if(sound == SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER) playVolume = ama$getThunderVolume();
        else if (sound == SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT) playVolume = ama$getImpactVolume();

        instance.playSound(x, y, z, sound, ama$getSoundCategory(), (float) playVolume, pitch, useDistance);
    }
}
