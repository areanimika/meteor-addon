package com.areanimika.areanimikahax.mixinterface;

import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.sound.SoundCategory;

public interface ILightningEntity {
    Color ama$getColorOverride();

    void ama$setColor(Color color);

    double ama$getThunderVolume();

    void ama$setThunderVolume(double volume);

    double ama$getImpactVolume();

    void ama$setImpactVolume(double volume);

    SoundCategory ama$getSoundCategory();

    void ama$setSoundCategory(SoundCategory category);
}
