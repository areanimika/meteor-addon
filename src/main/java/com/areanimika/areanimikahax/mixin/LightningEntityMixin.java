package com.areanimika.areanimikahax.mixin;

import com.areanimika.areanimikahax.mixinterface.ILightningEntity;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.entity.LightningEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LightningEntity.class)
public class LightningEntityMixin implements ILightningEntity {
    @Unique private Color color = null;

    @Override
    public Color ama$getColorOverride() {
        return color;
    }

    @Override
    public void ama$setColor(Color color) {
        this.color = color;
    }
}
